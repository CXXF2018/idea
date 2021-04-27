package com.atguigu.offline

import breeze.numerics.sqrt
import org.apache.spark.SparkConf
import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object ALSTrainer {

  //计算RMSE
  def getRMSE(model: MatrixFactorizationModel, testingRDD: RDD[Rating]):Double = {
    val userProducts = testingRDD.map(item=>(item.user,item.product))
    val predictProducts: RDD[Rating] = model.predict(userProducts)

    val real: RDD[((Int, Int), Double)] = testingRDD.map(item=>((item.user,item.product),item.rating))
    val predict: RDD[((Int, Int), Double)] = predictProducts.map(item=>((item.user,item.product),item.rating))

    //计算RMSE
    sqrt(
      real.join(predict).map{
        case ((userId,productId),(real,predict))=>
          //真实值和预测值之间的差
            val err = real-predict
            err*err
      }.mean()
    )
  }

  // 输出最终的最优参数
  def adjustALSParams(trainningRDD: RDD[Rating], testingRDD: RDD[Rating]) :Unit = {
    // 这里指定迭代次数为5，rank和lambda在几个值中选取调整
    val result: Array[(Int, Double, Double)] = for (rank <- Array(100, 200, 250); lambda <- Array(1, 0.1, 0.01, 0.001))
      yield {
        val model: MatrixFactorizationModel = ALS.train(trainningRDD, rank, 5, lambda)
        val rmse: Double = getRMSE(model, testingRDD)
        (rank, lambda, rmse)
      }

    //输出最佳值
    println(result.sortBy(_._3).head)


  }

  def main(args: Array[String]): Unit = {
    val config = Map(
      "spark.cores" -> "local[*]",
      "mongo.uri" -> "mongodb://localhost:27017/recommender",
      "mongo.db" -> "recommender"
    )

    //创建SparkConf
    val sparkConf = new SparkConf().setAppName("ALSTrainer").setMaster(config("spark.cores"))
    //创建SparkSession
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()

    //创建数据库配置信息对象
    val mongoConfig = MongoConfig(config("mongo.uri"),config("mongo.db"))

    import spark.implicits._

    //加载评分数据
    val ratingRDD: RDD[Rating] = spark.read
      .option("uri", mongoConfig.uri)
      .option("collection", OfflineRecommender.MONGODB_RATING_COLLECTION)
      .format("com.mongodb.spark.sql")
      .load()
      .as[ProductRating]
      .rdd
      .map(rating => Rating(rating.userId, rating.productId, rating.score))
      .cache()

    //切分数据集为训练集和测试集
    val splits: Array[RDD[Rating]] = ratingRDD.randomSplit(Array(0.8,0.2))
    val trainningRDD: RDD[Rating] = splits(0)
    val testingRDD: RDD[Rating] = splits(1)

    //输出最优参数
    adjustALSParams(trainningRDD,testingRDD)

    //关闭spark
    spark.close()
  }
}
