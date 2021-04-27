package com.atguigu.offline

import org.apache.spark.SparkConf
import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.jblas.DoubleMatrix

case class Movie(mid: Int, name: String, descri: String, timelong: String, issue: String,
                 shoot: String,language: String, genres: String, actors: String, directors: String )

case class MovieRating(uid: Int, mid: Int, score: Double, timestamp: Int)

case class MongoConfig(uri:String, db:String)

//标准推荐对象
case class Recommendation(mid: Int, score:Double)

//用户推荐对象
case class UserRecs(uid: Int, recs: Seq[Recommendation])

// 电影相似度（电影推荐）

case class MovieRecs(mid: Int, recs: Seq[Recommendation])

object OfflineRecommender {

  // 计算两个电影之间的余弦相似度
  def consinSim(movie1: DoubleMatrix, movie2:DoubleMatrix) : Double ={
    movie1.dot(movie2) / ( movie1.norm2()  * movie2.norm2() )
  }

  // 定义常量
  val MONGODB_RATING_COLLECTION = "Rating"
  val MONGODB_MOVIE_COLLECTION = "Movie"

  // 推荐表的名称
  val USER_RECS = "UserRecs"
  val MOVIE_RECS = "MovieRecs"
  val USER_MAX_RECOMMENDATION = 20

  def main(args: Array[String]): Unit = {

    // 定义配置
    val config = Map(
      "spark.cores" -> "local[*]",
      "mongo.uri" -> "mongodb://localhost:27017/recommender",
      "mongo.db" -> "recommender"
    )

    //创建sparkConfig
    val sparkConf: SparkConf = new SparkConf().setMaster(config("spark.cores")).setAppName("StatisticsRecommender")

    //创建sparkSession
    val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

    implicit val mongoConfig =MongoConfig(config("mongo.uri"),config("mongo.db"))

    //声明隐式转换
    import spark.implicits._

    //读取mongoDB中的电影数据
    val movieRDD: RDD[Int] = spark.read
      .option("uri", mongoConfig.uri)
      .option("collection", MONGODB_MOVIE_COLLECTION)
      .format("com.mongodb.spark.sql")
      .load()
      .as[Movie].rdd.map(_.mid).cache()

    //读取mongoDB中的业务数据
    val ratingRDD: RDD[(Int, Int, Double)] = spark.read
      .option("uri", mongoConfig.uri)
      .option("collection", MONGODB_RATING_COLLECTION)
      .format("com.mongodb.spark.sql")
      .load()
      .as[MovieRating]
      .rdd.map(rating => (rating.uid, rating.mid, rating.score)).cache()

    //用户的数据集
    val userRDD: RDD[Int] = ratingRDD.map(_._1).distinct()

    //创建训练数据集,此处的Rating并不是自己创建的，而是mlib自带的
    val trainData: RDD[Rating] = ratingRDD.map(x=>Rating(x._1,x._2,x._3))

    //准备参数
    //rank为模型中隐语义因子的个数，iterations是迭代次数，lambda是ALS的正则化参
    val (rank,iterations,lambda) = (50,5,0.1)

    //调用ALS算法训练隐语义模型
    val model: MatrixFactorizationModel = ALS.train(trainData,rank,iterations,lambda)

    //计算用户推荐矩阵
    val userMovies: RDD[(Int, Int)] = userRDD.cartesian(movieRDD)

    //model已经训练好，把id传进去可以得到预测评分列表RDD【Rating】（uid，mid，rating）
    val preRatings: RDD[Rating] = model.predict(userMovies)//?????为什么传入空矩阵

    val userRecs: DataFrame = preRatings.filter(_.rating > 0).map(rating => (rating.user, (rating.product, rating.rating)
    )).groupByKey().map {
      case (uid, recs) => UserRecs(uid, recs.toList.sortWith(_._2 > _._2).take(USER_MAX_RECOMMENDATION)
        .map(x => Recommendation(x._1, x._2)))
    }.toDF()

    userRecs.write
      .option("uri",mongoConfig.uri)
      .option("collection",USER_RECS)
      .mode("overwrite")
      .format("com.mongodb.spark.sql")
      .save()

    //TODO 计算相似度矩阵
    //过 ALS 计算电影见相似度矩阵，该矩阵用于查询当前电影的相似电影并为实时推荐系统服务
    val movieFeatures: RDD[(Int, DoubleMatrix)] = model.productFeatures.map {
      // 获取电影的特征矩阵，数据格式 RDD[(scala.Int, scala.Array[scala.Double])]
      case (mid, features) => (mid, new DoubleMatrix(features))
    }
    movieFeatures

    //计算笛卡尔乘积并过滤合并
    val movieRecs: DataFrame = movieFeatures.cartesian(movieFeatures).filter {
      case (a, b) => a._1 != b._1
    }.map {
      case (a, b) =>
        val simScore = this.consinSim(a._2, b._2) //求余弦相似度
        (a._1, (b._1, simScore))
    }.filter(_._2._2 > 0.6).groupByKey().map {
      case (mid, items) =>
        MovieRecs(mid, items.toList.map(x => Recommendation(x._1, x._2)))
    }.toDF()

    movieRecs.write
      .option("uri", mongoConfig.uri)
      .option("collection",MOVIE_RECS)
      .mode("overwrite")
      .format("com.mongodb.spark.sql")
      .save()

    //关闭spark
    spark.stop()

  }
}
