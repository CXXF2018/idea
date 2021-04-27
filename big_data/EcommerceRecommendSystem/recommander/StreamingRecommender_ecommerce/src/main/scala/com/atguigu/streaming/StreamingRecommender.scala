package com.atguigu.streaming


import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.{MongoClient, MongoClientURI, MongoCollection}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import redis.clients.jedis.Jedis

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

//MongoDB连接对象
object ConnHelper extends Serializable{
  lazy val jedis = new Jedis("localhost")
  lazy val mongoClient = MongoClient(MongoClientURI("mongodb://localhost:27017/recommender"))
}

//mongo参数
case class MongoConfig(uri:String,db:String)

//标准推荐
case class Recommendation(productId:Int,socre:Double)

//用户推荐
case class UserRecs(userId:Int,recs: Seq[Recommendation])

//产品相似度
case class ProductRecs(productId:Int,recs:Seq[Recommendation])

object StreamingRecommender {

  val MAX_SIM_PRODUCTS_NUM = 20
  val MONGODB_STREAM_RECS_COLLECTION = "streamRercs"
  val MONGODB_RATING_COLLECTION = "rating"
  val MONGODB_PRODUCTS_RECS_COLLECTION = "productsRecs"

  import scala.collection.JavaConversions._
  /**
    * 获取当前最近的M次商品评分
    * @param num  评分的个数
    * @param userId  谁的评分
    * @return
    */
  def getUserRecentlyRating(num:Int, userId:Int,jedis:Jedis): Array[(Int,Double)] ={
    //从用户的队列中取出num个评分
    jedis.lrange("userId:"+userId.toString,0,num).map{
      item=>
        var attr = item.split("\\:")
        (attr(0).trim.toInt,attr(1).trim.toDouble)
    }.toArray
  }

  /**
    * 获取当前商品K个相似的商品
    * @param num          相似商品的数量
    * @param productId          当前商品的ID
    * @param userId          当前的评分用户
    * @param simProducts    商品相似度矩阵的广播变量值
    * @param mongoConfig   MongoDB的配置
    * @return
    */
  def getTopSimProducts(num:Int, productId:Int, userId:Int, simProducts:scala.collection.Map[Int,
    scala.collection.immutable.Map[Int,Double]])(implicit mongoConfig: MongoConfig): Array[Int] ={
    //从广播变量的商品相似度矩阵中获取当前商品所有的相似商品
    val allSimProducts: Array[(Int, Double)] = simProducts.get(productId).get.toArray

    //获取用户已经观看过的商品
    val ratingExist: Array[Int] = ConnHelper.mongoClient(mongoConfig.db)(MONGODB_RATING_COLLECTION).find(MongoDBObject
    ("userId" -> userId)).toArray.map {
      item => item.get("productId").toString.toInt
    }

    allSimProducts.filter(x=> !ratingExist.contains(x._1)).sortWith(_._2>_._2).take(num).map(x=>x._1)
  }

  /**
    * 计算待选商品的推荐分数
    * @param simProducts            商品相似度矩阵
    * @param userRecentlyRatings  用户最近的k次评分
    * @param topSimProducts         当前商品最相似的K个商品
    * @return
    */
  def computeProductScores(simProducts:scala.collection.Map[Int,scala.collection.immutable.Map[Int,Double]],userRecentlyRatings:Array[(Int,Double)],topSimProducts: Array[Int]): Array[(Int,Double)] ={
    //用于保存每一个待选商品和最近评分的每一个商品的权重得分
    val score: ArrayBuffer[(Int, Double)] = ArrayBuffer[(Int,Double)]()

    //增强因子
    val increMap: mutable.HashMap[Int, Int] = mutable.HashMap[Int,Int]()

    //减弱因子
    val descMap: mutable.HashMap[Int, Int] = mutable.HashMap[Int,Int]()

    for (topSimProduct<-topSimProducts;userRecentlyRating<-userRecentlyRatings) {
      val simScore: Double = getProductsSimScore(simProducts, userRecentlyRating._1, topSimProduct)

      if(simScore>0.6){
        score +=((topSimProduct,simScore*userRecentlyRating._2))
        if (userRecentlyRating._2>3){
          increMap(topSimProduct) = increMap.getOrDefault(topSimProduct,0)+1
        }else{
          descMap(topSimProduct) = descMap.getOrDefault(topSimProduct,0)+1
        }
      }
    }

    score.groupBy(_._1).map{
      case (productId,sims)=>(productId,sims.map(_._2).sum/sims.length + log(increMap.getOrDefault
      (productId,1))- log(descMap.getOrDefault(productId,1)))
    }.toArray.sortWith(_._2 > _._2)
  }

  //取10的对数
  def log(m:Int):Double ={
    math.log(m) / math.log(10)
  }

  /**
    * 获取当个商品之间的相似度
    * @param simProducts       商品相似度矩阵
    * @param userRatingProduct 用户已经评分的商品
    * @param topSimProduct     候选商品
    * @return
    */
  def getProductsSimScore(simProducts:scala.collection.Map[Int,scala.collection.immutable.Map[Int,Double]], userRatingProduct:Int, topSimProduct:Int): Double ={
    simProducts.get(topSimProduct) match {
      case Some(sim) => sim.get(userRatingProduct) match {
        case Some(score) => score
        case None => 0.0
      }
      case None => 0.0
    }
  }

  /**
    * 将数据保存到MongoDB    userId -> 1,  recs -> 22:4.5|45:3.8
    * @param streamRecs  流式的推荐结果
    * @param mongoConfig  MongoDB的配置
    */
  def saveRecsToMongoDB(userId:Int,streamRecs:Array[(Int,Double)])(implicit
                                                                   mongoConfig: MongoConfig)
  : Unit ={
    val streamRecsCollection: MongoCollection = ConnHelper.mongoClient(mongoConfig.db)(MONGODB_STREAM_RECS_COLLECTION)

    streamRecsCollection.findAndRemove(MongoDBObject("userId"->userId))
    streamRecsCollection.insert(MongoDBObject("userId" -> userId, "recs"->streamRecs.map
    (x=>MongoDBObject("productId"->x._1,"score"->x._2))))
  }

    def main(args: Array[String]): Unit = {
      val config = Map(
        "spark.cores" -> "local[*]",
        "mongo.uri" -> "mongodb://localhost:27017/recommender",
        "mongo.db" -> "recommender",
        "kafka.topic" -> "recommender"
      )

      //创建一个SparkConf配置
      val sparkConf = new SparkConf().setAppName("StreamingRecommender").setMaster(config("spark.cores"))
      val spark = SparkSession.builder().config(sparkConf).getOrCreate()

      val sc: SparkContext = spark.sparkContext
      val ssc = new StreamingContext(sc,Seconds(2))

      implicit val mongoConfig = MongoConfig(config("mongo.uri"),config("mongo.db"))

      import spark.implicits._

      //广播商品相似度矩阵
      //转换成Map【Int,Map【Int，Double】】
      val simProductsMatrix: collection.Map[Int, Map[Int, Double]] = spark.read
        .option("uri", mongoConfig.uri)
        .option("collection", MONGODB_PRODUCTS_RECS_COLLECTION)
        .format("com.mongodb.spark.sql")
        .load()
        .as[ProductRecs]
        .rdd
        .map {
          recs => (recs.productId, recs.recs.map(x => (x.productId, x.socre)).toMap)
        }.collectAsMap()

      val simProductsMatrixBroadCast: Broadcast[collection.Map[Int, Map[Int, Double]]] = sc.broadcast(simProductsMatrix)

      //创建Kafka连接
      val kafkaPara = Map(
        "bootstrap.servers" -> "localhost:9092",
        "key.deserializer" -> classOf[StringDeserializer],
        "valuedeserializer" -> classOf[StringDeserializer],
        "group.id" -> "recommender",
        "auto.offset.reset" -> "latest"
      )

      val kafkaStream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](ssc, LocationStrategies.PreferConsistent,
        ConsumerStrategies.Subscribe[String, String](Array(config("kafka.topic")), kafkaPara))
      kafkaStream

      //产生评分流
      //UID|MID|SCORE|TIMESTAMP
      val ratingStream: DStream[(Int, Int, Double, Int)] = kafkaStream.map {
        case msg =>
          var attr = msg.value().split("\\|")
          (attr(0).toInt, attr(1).toInt, attr(2).toDouble, attr(3).toInt)
      }
      ratingStream

      //核心实时推荐算法
      ratingStream.foreachRDD{
        rdd=>rdd.map{
          case (userId,productId,score,timestamp)=>
            println(">>>>>>>>>>>>>>>>>>")

          //获取当前最近的M次商品评分
          val userRecentlyRatings = getUserRecentlyRating(MAX_SIM_PRODUCTS_NUM,userId,ConnHelper.jedis)

          //获取商品P最相似的K个商品
          val simProducts = getTopSimProducts(MAX_SIM_PRODUCTS_NUM,productId,userId, simProductsMatrixBroadCast.value)

          //计算待选商品的优先级
          val streamRecs = computeProductScores(simProductsMatrixBroadCast.value,userRecentlyRatings,simProducts)

          //将数据保存到MongoDB中
            saveRecsToMongoDB(userId,streamRecs)
        }.count()
      }

      //启动Streaming程序
      ssc.start()
      ssc.awaitTermination()
  }
}
