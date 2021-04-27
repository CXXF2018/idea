package com.atguigu.offline

import breeze.numerics.sqrt
import org.apache.spark.SparkConf
import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object ALSTrainer {


  def getRMSE(model: MatrixFactorizationModel, data: RDD[Rating]):Double = {
    val userMovies: RDD[(Int, Int)] = data.map(items=>(items.user,items.product))
    val predictRating: RDD[Rating] = model.predict(userMovies)
    val real: RDD[((Int, Int), Double)] = data.map(items=>((items.user,items.product),items.rating))
    val predict: RDD[((Int, Int), Double)] = predictRating.map(items=>((items.user,items.product),items.rating))

    //计算RMSE
    sqrt(
      real.join(predict).map{
        case((uid,mid),(real,predict)) =>
          //真实值和预测值之间的差
        var err = real - predict

          err*err
      }.mean()
    )
  }

  def adjustALSParams(trainingRDD: RDD[Rating], testingRDD: RDD[Rating]) = {
    // 这里指定迭代次数为 5 ，rank和 lambda在几个值中选取调整
    val result = for(rank<-Array(100,200,250);lambda<-Array(1,0.1,0.01,0.001))
      yield {
        val model: MatrixFactorizationModel = ALS.train(trainingRDD,rank,5,lambda)
        val rmse = getRMSE(model,testingRDD)
      }

    //按照 rmse排序
    println(result.sortBy(_._3).head)


  }

  def main(args: Array[String]): Unit = {
    //配置信息
    val config = Map(
      "spark.cores" -> "local[*]",
      "mongo.uri" -> "mongodb://localhost:27017/recommender",
      "mongo.db" -> "recommender"
    )

    //创建saprkConf
    val sparkConf: SparkConf = new SparkConf().setMaster(config("spark.cores")).setAppName("ALSTrainer")

    //创建SparkSession
    val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

    val mongoConfig = MongoConfig(config("mongo.uri"),config("mongo.db"))

    //加载评分数据
    val ratingRDD = spark
      .read
      .option("uri",mongoConfig.uri)
      .option("collection",OfflineRecommender.MONGODB_RATING_COLLECTION)
      .format("com.mongodb.spark.sql")
      .load()
      .as[MovieRating]
      .rdd
      .map(rating => Rating(rating.uid,rating.mid,rating.score)).cache()

    // 将一个 RDD随机切分成两个 RDD ， 用以划分训练集和测试集
    val splits = ratingRDD.randomSplit(Array(0.8,0.2))
    val trainingRDD = splits(0)
    val testingRDD = splits(1)

    //输出最优参数
    adjustALSParams(trainingRDD,testingRDD)

    //关闭spark
    spark.close()
  }

}
