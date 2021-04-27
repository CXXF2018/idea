package com.atguigu.offline

import breeze.linalg.rank
import org.apache.spark.SparkConf
import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.jblas.DoubleMatrix

//需要导入的数据格式
case class ProductRating(userId:Int,productId:Int,score:Double,timestamp:Int)

数据库参数
case class MongoConfig(uri:String,db:String)

//标准推荐对象
case class Recommendation(productId:Int,score:Double)

//商品相似度
case class ProductRecs(productId:Int,recs:Seq[Recommendation])

//用户推荐列表
case class UserRecs(userId:Int,recs:Seq[Recommendation])

object OfflineRecommender {
  //计算两个商品之间的余弦相似度
  def consinSim(product1: DoubleMatrix, product2:DoubleMatrix) : Double ={
    product1.dot(product2) / ( product1.norm2()  * product2.norm2() )
  }

  //定义常量
  val MONGODB_RATING_COLLECTION = "Rating"

  //推荐表的名称
  val USER_RECS = "UserRecs"
  val PRODUCT_RECS = "ProductRecs"

  val USER_MAX_RECOMMENDATION = 20

  def main(args: Array[String]): Unit = {
    //定义配置
    val config = Map(
      "spark.cores" -> "local[*]",
      "mongo.uri" -> "mongodb://localhost:27017/recommender",
      "mongo.db" -> "recommender"
    )

    //创建SparkConfig
    val sparkConf: SparkConf = new SparkConf().setMaster(config("spark.cores")).setAppName("OfflineRecommender")

    //创建SparkSession
    val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

    implicit val mongoConfig = MongoConfig(config("mongo.uri"),config("mongo.db"))

    import spark.implicits._

    //读取数据库中的数据
    val ratingRDD: RDD[(Int, Int, Double)] = spark.read
      .option("uri", mongoConfig.uri)
      .option("collection", MONGODB_RATING_COLLECTION)
      .format("com.mongodb.spark.sql")
      .load()
      .as[ProductRating]
      .rdd
      .map(rating => (rating.userId, rating.productId, rating.score)).cache()

    //用户数据集
    val userRDD: RDD[Int] = ratingRDD.map(_._1).distinct()

    //商品数据集
    val productRDD: RDD[Int] = ratingRDD.map(_._2).distinct()

    //创建训练数据集
    val trainData: RDD[Rating] = ratingRDD.map(x=>Rating(x._1,x._2,x._3))

    //定义ALS参数,rank 是模型中隐语义因子的个数, iterations 是迭代的次数, lambda 是ALS的正则化参
    val (rank,iterations,lambda) = (50,5,0.01)

    //训练模型
    val model: MatrixFactorizationModel = ALS.train(trainData,rank,iterations,lambda)

    //计算用户推荐矩阵
    val userProducts: RDD[(Int, Int)] = userRDD.cartesian(productRDD)

    val preRatings: RDD[Rating] = model.predict(userProducts)

    val userRecs: DataFrame = preRatings.filter(_.rating > 0)
      .map(rating => (rating.user, (rating.product, rating.rating)))
      .groupByKey()
      .map {
        case (userId, recs) => UserRecs(userId, recs.toList.sortWith(_._2 > _._2).take
        (USER_MAX_RECOMMENDATION).map(x => Recommendation(x._1, x._2)))
      }.toDF()

    userRecs.write
      .option("uri",mongoConfig.uri)
      .option("collection",USER_RECS)
      .mode("overwrite")
      .format("com.mongodb.spark.sql")
      .save()

    //TODO 计算商品相似度
    //获取商品的特征矩阵，数据格式为(Int,Array[Double])
    val productFeatures: RDD[(Int, DoubleMatrix)] = model.productFeatures.map {
      case (productId, features) => (productId, new DoubleMatrix(features))
    }

    //商品与商品集合计算笛卡尔乘积，并过滤掉不符合条件的值
    val productRecs: DataFrame = productFeatures.cartesian(productFeatures).filter {
      case (a, b) => a._1 != b._1
    }.map {
      case (a, b) =>
        val simScore = this.consinSim(a._2, b._2)
        (a._1, (b._1, simScore))
    }.filter(_._2._2 > 0.6)
      .groupByKey()
      .map { case (prodictId, items) => ProductRecs(prodictId, items.toList.map(x => Recommendation(x
        ._1, x._2)))}.toDF()

    productRecs.write
      .option("uri", mongoConfig.uri)
      .option("collection",PRODUCT_RECS)
      .mode("overwrite")
      .format("com.mongodb.spark.sql")
      .save()

    //关闭spark
    spark.stop()

  }
}
