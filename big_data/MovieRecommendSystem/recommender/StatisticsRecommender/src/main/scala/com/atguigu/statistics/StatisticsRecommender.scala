package com.atguigu.statistics

import java.text.SimpleDateFormat
import java.util.Date

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}


//定义样例类
//电影数据表和评分表
case class Movie(mid: Int, name: String, descri: String, timelong: String, issue: String,
                 shoot: String, language: String, genres: String, actors: String, directors: String)

case class Rating(uid: Int, mid: Int, score: Double, timestamp: Int)

//mongoDB的参数信息
case class MongoConfig(uri:String,db:String)

//推荐信息
case class Recommendation(mid:Int, score:Double)
case class GenresRecommendation(genres:String, recs:Seq[Recommendation])

object StatisticsRecommender{

  //定义表的名称
  val MONGODB_RATING_COLLECTION = "Rating"
  val MONGODB_MOVIE_COLLECTION = "Movie"

  val RATE_MORE_MOVIES = "RateMoreMovies"
  val RATE_MORE_RECENTLY_MOVIES = "RateMoreRecentlyMovies"
  val AVERAGE_MOVIES = "AverageMovies"
  val GENRES_TOP_MOVIES = "GenresTopMovies"

  def main(args: Array[String]): Unit = {

    //定义spark信息和mongo信息
    val config = Map(
      "spark.cores"->"local[*]",
      "mongo.uri"->"mongodb://localhost:27017/recommender",
      "mongo.db"->"recommender"
    )

    //创建sparkConfig
    val sparkConf: SparkConf = new SparkConf().setMaster(config("spark.cores")).setAppName("StatisticsRecommender")

    //创建sparkSession
    val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

    //获取mongo是数据库的连接配置信息
    val mongoConfig = MongoConfig(config("mongo.uri"),config("mongo.db"))

    //声明隐式转换所需要的类
    import spark.implicits._

    //读出相关表的数据，并转换为DF类型，这些表分别是：评分表，电影信息表
    val ratingDF = spark.read
      .option("uri",config("mongo.uri"))
      .option("collection",MONGODB_RATING_COLLECTION)
      .format("com.mongodb.spark.sql")
      .load()
      .as[Rating].toDF()

    val movieDF = spark
      .read
      .option("uri",mongoConfig.uri)
      .option("collection",MONGODB_MOVIE_COLLECTION)
      .format("com.mongodb.spark.sql")
      .load()
      .as[Movie]
      .toDF()

    //创建一张ratings表
    ratingDF.createOrReplaceTempView("ratings")

    //TODO 不同的统计推荐结果
    //1.历史热门电影统计
    val rateMoreMoviesDF = spark.sql("select mid,count(mid) as count from ratings group by mid")

    rateMoreMoviesDF.write
      .option("uri",mongoConfig.uri)
      .option("collection",RATE_MORE_MOVIES)
        .mode("overwrite")
      .format("com.mongodb.spark.sq")
      .save()

    //2.最近热门电影统计
    //创建一个日期格式化工具
    val simpleDateFormat = new SimpleDateFormat("yyyyMM")

    //注册一个UDF函数，用于将timestamp转换成年月格式
    spark.udf.register("changeDate",(x:Int)=>simpleDateFormat.format(new Date(x*1000L)).toInt)

    //将原来的timestamp转换成年月格式
    val ratingsYearMonth: DataFrame = spark.sql("select mid,score,changeDate(timestamp) as yearmonth from ratings")

    //将新的数据注册成一张表
    ratingsYearMonth.createOrReplaceTempView("ratingOfMonth")

    val rateMoreRecentlyMovies: DataFrame = spark.sql("select mid count(mid) as count,yearmonth " +
      "from ratingOfMonth group by yearmonth,mid")

    //将得到的结果存储在mongoDB
    rateMoreRecentlyMovies.write
      .option("uri",mongoConfig.uri)
      .option("collection",RATE_MORE_RECENTLY_MOVIES)
      .mode("overwrite")
      .format("com.mongo.db.spark.sql")
      .save()

    //电影平均得分统计
    val averageMoviesDF: DataFrame = spark.sql("select mid,avg(score) as avg from ratings group by mid")
    averageMoviesDF.write
      .option("uri",mongoConfig.uri)
      .option("collection",AVERAGE_MOVIES)
      .mode("overwrite")
      .format("com.mongo.db.spark.sql")
      .save()

    //每个类别优质电影统计，统计每种电影中评分最高的10个

    val movieWithScore: DataFrame = movieDF.join(averageMoviesDF,Seq("mid"))

    //所有的电影类别,并将其转化为RDD
    val genres = List("Action","Adventure","Animation","Comedy","Crime","Documentary",
      "Drama","Family", "Fantasy","Foreign","History","Horror","Music","Mystery"
      ,"Romance","Science","Tv","Thriller","War","Western")

    val genresRDD: RDD[String] = spark.sparkContext.makeRDD(genres)

    //将影片集合和电影类型做笛卡尔乘积，然后筛选掉不符合条件的数据
    val genrenTopMovies: DataFrame = genresRDD.cartesian(movieWithScore.rdd).filter {
      case (genres, row) => row.getAs[String]("genres").toLowerCase().contains(genres.toLowerCase())
    }.map {
      //将整个数据集缩小，生成RDD
      case (genres, row) => {
        (genres, (row.getAs[Int]("mid"), row.getAs[Double]("avg")))
      }
    }.groupByKey().map {
      case (genres, items) => GenresRecommendation(genres, items.toList.sortWith(_._2 > _._2).take(10)
        .map(item => Recommendation(item._1, item._2)))
    }.toDF()
    genrenTopMovies

    genrenTopMovies.write
      .option("uri",mongoConfig.uri)
      .option("collection",GENRES_TOP_MOVIES)
      .mode("overwrite")
      .format("com.mongodb.spark.sql")
      .save()

    //关闭spark
    spark.stop()
  }
}
