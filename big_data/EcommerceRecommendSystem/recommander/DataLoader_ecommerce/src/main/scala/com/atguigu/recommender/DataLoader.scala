package com.atguigu.recommender

import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.{MongoClient, MongoClientURI, MongoCollection}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

//为两个数据集,MongoDB配置信息准备样例类
case class Product(productId:Int,name:String,imageUrl:String,categories:String,tags:String)
case class Rating(userId:Int,productId:Int,score:Double,timestamp:Int)

case class MongoConfig(uri:String,db:String)

object DataLoader {

  //数据路径
  val PRODUCT_DATA_PATH = "E:\\IdeaProjects\\big_data\\EcommerceRecommendSystem\\recommander\\DataLoader_ecommerce\\src\\main\\resources\\products.csv"
  val RATING_DATA_PATH = "E:\\IdeaProjects\\big_data\\EcommerceRecommendSystem\\recommander\\DataLoader_ecommerce\\src\\main\\resources\\ratings.csv"

  //用于MongoDB的数据Collection名
  val MONGODB_PRODUCT_COLLECTION = "Product"
  val MONGODB_RATING_COLLECTION = "Rating"

  def storeDataIntoMongoDB(productDF: DataFrame, ratingDF: DataFrame)(implicit mongoConfig: MongoConfig):Unit={
    //建立MongoDB数据库的连接
    val mongoClient = MongoClient(MongoClientURI(mongoConfig.uri))

    //定义通过MongoDB客户端拿到的表操作对象
    val productCollection: MongoCollection = mongoClient(mongoConfig.db)(MONGODB_PRODUCT_COLLECTION)
    val ratingCollection: MongoCollection = mongoClient(mongoConfig.db)(MONGODB_RATING_COLLECTION)

    //如果MongoDB中有对应的数据库，那么应该删除
    productCollection.dropCollection()
    ratingCollection.dropCollection()

    //将当前数据写入到MongoDB
    productDF.write
      .option("uri",mongoConfig.uri)
      .option("collection",MONGODB_PRODUCT_COLLECTION)
      .mode("overwrite")
      .format("com.mongodb.spark.sql")
      .save()

    ratingDF.write
      .option("uri",mongoConfig.uri)
      .option("collection",MONGODB_RATING_COLLECTION)
      .mode("overwrite")
      .format("com.mongodb.spark.sql")
      .save()

    //对数据表建索引
    productCollection.createIndex(MongoDBObject("productId"->1))
    ratingCollection.createIndex(MongoDBObject("userId"->1))
    ratingCollection.createIndex(MongoDBObject("productId"->1))


    //关闭数据库连接
    mongoClient.close()

  }

  def main(args: Array[String]): Unit = {

    //定义配置信息
    val config = Map {
      "spark.cores" -> "local[*]"
      ,
      "mongo.uri" -> "mongodb://localhost:27017/recommender"
      ,
      "mongo.db" -> "recommender"
    }

    //创建SparkConf
    val sparkConf: SparkConf = new SparkConf().setMaster(config("spark.cores")).setAppName("DataLoader")

    //创建SparkSession
    val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

    //在对 DataFrame和 Dataset进行操作许多操作都需要这个包进行支持
    import spark.implicits._

    //加载商品数据
    val productRDD: RDD[String] = spark.sparkContext.textFile(PRODUCT_DATA_PATH)

    //将商品数据RDD转换成DataFrame
    val productDF: DataFrame = productRDD.map(
      items => {
        val attr: Array[String] = items.split("\\^")
        Product(attr(0).toInt, attr(1).trim, attr(4).trim, attr(5).trim, attr(6).trim)
      }
    ).toDF()

    //加载用户评分数据，并进行转换
    val ratingRDD: RDD[String] = spark.sparkContext.textFile(RATING_DATA_PATH)
    val ratingDF: DataFrame = ratingRDD.map(
      items => {
        val attr: Array[String] = items.split("\\^")
        Rating(attr(0).toInt, attr(1).toInt, attr(2).toDouble, attr(3).toInt)
      }
    ).toDF()

    //声明一个隐式的数据库配置对象
    implicit val mongoConfig = MongoConfig(config("mongo.uri"),config("mongo.db"))

    //将数据导入MongoDB数据库中
    storeDataIntoMongoDB(productDF,ratingDF)

    //关闭Spark
    spark.stop()

}

}
