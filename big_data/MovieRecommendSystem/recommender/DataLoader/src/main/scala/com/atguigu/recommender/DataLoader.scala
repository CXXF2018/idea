package com.atguigu.recommender

import java.net.InetAddress

import com.mongodb.MongoClientURI
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.{MongoClient, MongoClientURI}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.index.analysis.PreBuiltAnalyzerProviderFactory
import org.elasticsearch.transport.client.PreBuiltTransportClient

//定义样例类
case class Movie(mid:Int,name:String,descri:String,timelong:String,shoot:String,issue:String,
                 language:String,genres:String,director:String,actors:String)
case class Rating(uid:Int,mid:Int,score:Double,timestamp:Int)
case class Tag(uid:Int,mid:Int,tag:String,timestamp:Int)
case class MongoConfig(uri:String,db:String)
/**
  *
  * @param httpHosts  http主机列表，逗号分隔
  * @param transportHosts transport主机列表，集群彼此之间做内部的数据传输
  * @param index  需要操作的索引
  * @param clustername  集群名称，默认
  */
case class ESConfig(httpHosts:String,transportHosts:String,index:String,clustername:String)


object DataLoader {

  //配置文件路径
  val MOVIE_DATA_PATH = "E:\\IdeaProjects\\big_data\\MovieRecommendSystem\\recommender\\DataLoader\\src\\main\\resources\\movies.csv"
  val RATING_DATA_PATH = "E:\\IdeaProjects\\big_data\\MovieRecommendSystem\\recommender\\DataLoader\\src\\main\\resources\\ratings.csv"
  val TAG_DATA_PATH = "E:\\IdeaProjects\\big_data\\MovieRecommendSystem\\recommender\\DataLoader\\src\\main\\resources\\tags.csv"

  val MONGODB_MOVIE_COLLECTION = "Movie"
  val MONGODB_RATING_COLLECTION = "Rating"
  val MONGODB_TAG_COLLECTION = "Tag"
  val ES_MOVIE_INDEX = "Movie"

  def storeDataInMongoDB(movieDF: DataFrame, ratingDF: DataFrame, tagDF: DataFrame)(implicit
                                                                                    mongoConfig:MongoConfig): Unit = {

    //新建一个到MongoDB的连接
    val mongoClient = MongoClient(MongoClientURI(mongoConfig.uri))

    //如果对应的数据库已经存在，则删除
    mongoClient(mongoConfig.db)(MONGODB_MOVIE_COLLECTION).dropCollection()
    mongoClient(mongoConfig.db)(MONGODB_RATING_COLLECTION).dropCollection()
    mongoClient(mongoConfig.db)(MONGODB_TAG_COLLECTION).dropCollection()

    //将当前数据写到Mongo
    movieDF.write
      .option("uri",mongoConfig.uri)
      .option("collection",MONGODB_MOVIE_COLLECTION)
      .mode("overwrate")
      .format("com.mongodb.spark.sql")
      .save()

    ratingDF
      .write
      .option("uri",mongoConfig.uri)
      .option("collection",MONGODB_RATING_COLLECTION)
      .mode("overwrite")
      .format("com.mongodb.spark.sql")
      .save()

    tagDF
      .write
      .option("uri",mongoConfig.uri)
      .option("collection",MONGODB_TAG_COLLECTION)
      .mode("overwrite")
      .format("com.mongodb.spark.sql")
      .save()

    //对数据表建立索引
    mongoClient(mongoConfig.db)(MONGODB_MOVIE_COLLECTION).createIndex(MongoDBObject("mid" -> 1))
      mongoClient(mongoConfig.db)(MONGODB_RATING_COLLECTION).createIndex(MongoDBObject("uid" -> 1))
      mongoClient(mongoConfig.db)(MONGODB_RATING_COLLECTION).createIndex(MongoDBObject("mid" -> 1))
      mongoClient(mongoConfig.db)(MONGODB_TAG_COLLECTION).createIndex(MongoDBObject("uid" -> 1))
      mongoClient(mongoConfig.db)(MONGODB_TAG_COLLECTION).createIndex(MongoDBObject("mid" -> 1))

      //关闭Mongo连接
      mongoClient.close()

  }

  //通过Spark SQL提供的write方法进行数据的分布式插入
  def storeDataInES(movieDF: DataFrame)(implicit eSConfig: ESConfig): Unit = {

    //新建一个配置
    val settings: Settings = Settings.builder().put("cluster.name",eSConfig.clustername).build()

    //新建一个ES客户端
    val esClient = new PreBuiltTransportClient(settings)

    // 需要将 TransportHosts添加到 esClient中
    //正则表达式
    val REGEX_HOST_PORT = "(.+):(\\d+)".r
    eSConfig.transportHosts.split(",").foreach {
      case REGEX_HOST_PORT(host: String, port: String) => {
        esClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host),
          port.toInt))

      }
    }

    //清理掉ES中遗留的数据
    if (esClient.admin().indices().exists(new IndicesExistsRequest(eSConfig.index)).actionGet()
      .isExists){
      esClient.admin().indices().delete(new DeleteIndexRequest(eSConfig.index))
    }
    esClient.admin().indices().create(new CreateIndexRequest(eSConfig.index))

    //将数据写入ES中
    movieDF
      .write
      .option("es.nodes",eSConfig.httpHosts)
      .option("es.http.timeout","100m")
      .option("es.mapping.id","mid")
      .mode("overwrite")
      .format("org.elasticsearch.spark.sql")
      .save(eSConfig.index+"/"+ES_MOVIE_INDEX)

  }

  //主程序入口
  def main(args: Array[String]): Unit = {
    //定义用到的配置参数
    val config=Map(
      "spark.cores"->"local[*]",
      "mongo.uri" -> "mongodb://localhost:27017/recommender",
      "mongo.db" -> "recommender",
      "es.httpHosts" -> "localhost:9200",
      "es.transportHosts" -> "localhost:9300",
      "es.index" -> "recommender",
      "es.cluster.name" -> "elasticsearch"
    )

    //创建sparkConfig
    val sparkConf: SparkConf = new SparkConf().setAppName("DataLoader").setMaster(config("spark.cores"))

    //创建sparkSession
    val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

    //在对 DataFrame和 Dataset进行操作许多操作都需要这个包进行支持
    import spark.implicits._

    //todo 加载数据,将RDD数据转换为DataFrame类型
    val moiveRDD: RDD[String] = spark.sparkContext.textFile(MOVIE_DATA_PATH)
    val movieDF: DataFrame = moiveRDD.map(item => {
      val attr = item.split("\\^")
      Movie(attr(0).toInt, attr(1).trim, attr(2).trim, attr(3).trim, attr(4).trim,
        attr(5).trim, attr(6).trim, attr(7).trim, attr(8).trim, attr(9).trim)
    }).toDF()

    val ratingRDD: RDD[String] = spark.sparkContext.textFile(RATING_DATA_PATH)
    val ratingDF: DataFrame = ratingRDD.map(item => {
      val attr = item.split(",")
      Rating(attr(0).toInt, attr(1).toInt, attr(2).toDouble, attr(3).toInt)
    }).toDF()

    val tagRDD: RDD[String] = spark.sparkContext.textFile(TAG_DATA_PATH)
    val tagDF: DataFrame = tagRDD.map(item => {
      val attr = item.split(",")
      Tag(attr(0).toInt, attr(1).toInt, attr(2).trim, attr(3).toInt)
    }).toDF()

    //声明一个隐式的配置对象
    implicit val mongoConfig = MongoConfig(config("mongo.uri"), config("mongo.db"))

    //将数据保存到MongoDB
    storeDataInMongoDB(movieDF,ratingDF,tagDF)

    //将处理后的Tag数据，和Movie数据融合，产生新的Movie数据(在movieDF中加上一列，把movie对应的tag信息加进去tag1|tag2|tag3……）
    import org.apache.spark.sql.functions._

    val newTag=tagDF.groupBy($"mid")
      .agg(concat_ws("|",collect_set($"tag"))
      .as("tag")).select("mid","tags")

    val movieWithTagsDF: DataFrame = movieDF.join(newTag,Seq("mid","mid"),"left")

    //声明一个ES配置的隐式参数
    implicit val esConfig=ESConfig(config("es.httpHosts"),config("es.transportHosts"),config("es" +
      ".index"),config("es.cluster.name"))

    //将新的Moive数据保存到ES中
    storeDataInES(movieWithTagsDF)

    //关闭spark
    spark.stop()
  }
}
