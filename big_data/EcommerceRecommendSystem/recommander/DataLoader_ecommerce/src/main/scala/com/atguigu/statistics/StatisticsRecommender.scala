package com.atguigu.statistics

import java.sql.Date
import java.text.SimpleDateFormat

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

//创建用户评分样例类
case class Rating(userId:Int,productId:Int,score:Double,timestamp:Int)

//创建数据库相关信息样例类
case class MongoConfig(uri:String,db:String)

object StatisticsRecommender {

  //告知需要操作的表名
  val MONGODB_RATING_COLLECTION = "Rating"

  //需要统计的内容
  val RATE_MORE_PRODUCTS = "RateMoreProducts"
  val RATE_MORE_RECENTLY_PRODUCTS = "RateMoreRecentlyProducts"
  val AVERAGE_PRODUCTS = "AverageProducts"

  def main(args: Array[String]): Unit = {

    //配置信息
    val config = Map(
      "spark.cores" -> "local[*]",
      "mongo.uri" -> "mongodb://localhost:27017/recommender",
      "mongo.db" -> "recommender"
    )

    //创建SparkConfig
    val sparkConf: SparkConf = new SparkConf().setAppName("StatisticsRecommender").setMaster(config("spark.cores"))

    //创建SparkSession
    val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

    //创建MongoDB配置信息对象
    val mongoConfig = new MongoConfig(config("mongo.uri"),config("mongo.db"))

    //声明隐式转换
    import spark.implicits._

    //加载用户评分数据
    val ratingDF: DataFrame = spark.read
      .option("uri", mongoConfig.uri)
      .option("collection", MONGODB_RATING_COLLECTION)
      .load()
      .as[Rating]
      .toDF()

    //创建一张用户评分数据表
    ratingDF.createOrReplaceTempView("ratings")

    //TODO 计算不同的推荐结果
    //1.历史热门商品统计
    val rateMoreProductsDF: DataFrame = spark.sql("select productId,count(productId) as count " +
      "from ratings group by productId")

    rateMoreProductsDF.write
      .option("uri",mongoConfig.uri)
      .option("collection",RATE_MORE_PRODUCTS)
      .mode("overwrite")
      .format("com.mongo.db.spark.sql")
      .save()

    //2.最近热门商品统计（以月为单位）
    //日期格式化工具
    val simpleDateFormat = new SimpleDateFormat("yyyyMM")

    //注册一个UDF函数，用于将timestamp转换为对应的格式
    spark.udf.register("changeDate",(x:Int)=>simpleDateFormat.format(new Date(x*1000L)).toInt)

    //将原来的Ratings数据集中的时间转换为年月格式
    val ratingOfYearMonth: DataFrame = spark.sql("select productId,score,changeDate(timestamp) as yearmonth from ratings ")

    //将新的数据集注册成为一张表
    ratingOfYearMonth.createOrReplaceTempView("ratingOfMonth")

    val rateMoreRecentlyProducts: DataFrame = spark.sql("select productId,count(productId) as " +
      "count yearmonth from ratingOfMonth group by " +
      "productId," +
      "yearmonth order by yearmonth desc")

    //将数据写入数据库
    rateMoreRecentlyProducts.write
      .option("uri",mongoConfig.uri)
      .option("collection",RATE_MORE_RECENTLY_PRODUCTS)
      .mode("overwrite")
      .format("com.mongodb.spark.sql")
      .save()

    //3.商品平均分统计
    val averageProductsDF: DataFrame = spark.sql("select productId avg(score) as avg from ratings group by productId")

    averageProductsDF.write
      .option("uri",mongoConfig.uri)
      .option("collection",AVERAGE_PRODUCTS)
      .mode("overwrite")
      .format("com.mongo.spark.sql")
      .save()

    //关闭Spark
    spark.stop()
  }
}
