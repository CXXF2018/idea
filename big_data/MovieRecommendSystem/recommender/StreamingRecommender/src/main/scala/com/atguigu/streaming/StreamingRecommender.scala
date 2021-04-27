package com.atguigu.streaming

import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.{MongoClient, MongoClientURI}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.expressions.Log
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import redis.clients.jedis.Jedis

//创建连接助手对象
object ConnHander extends Serializable{
  lazy val jedis = new Jedis("localhost")
  lazy val mongoClient = MongoClient(MongoClientURI("mongodb://localhost:27017/recommender"))
}

case class MongoConfig(uri:String,db:String)

//标准推荐
case class Recommendation(mid:Int, score:Double)

//用户推荐
case class UserRecs(uid:Int, recs:Seq[Recommendation])

//相似电影推荐
case class MovieRecs(mid:Int, recs:Seq[Recommendation])

object StreamingRecommender {

  val MAX_USER_RATINGS_NUM = 20
  val MAX_SIM_MOVIES_NUM = 20
  val MONGODB_STREAM_RECS_COLLECTION = "StreamRecs"
  val MONGODB_RATING_COLLECTION = "Rating"
  val MONGODB_MOVIE_RECS_COLLECTION = "MovieRecs"

  import scala.collection.JavaConversions._//下面这个函数要用到，不可省略

  //获取当前最近的 M 次电影评分
  def getUserRecentlyRating(num: Int, uid: Int, jedis: Jedis): Array[(Int,Double)] ={
    //从用户队列中取出num个评分
    jedis.lrange("uid:"+uid.toString,0,num).map{
      item=>
        var attr =item.split("\\:")
        (attr(0).trim.toInt,attr(1).trim.toDouble)
    }.toArray
  }

  /**
    * 获取当前电影 K个相似的电影
    * @param num          相似电影的数量
    * @param mid          当前电影的 ID
    * @param uid          当前的评分用户
    * @param simMovies    电影相似度矩阵的广播变量值
    * @param mongoConfig   MongoDB 的配置
    * @return
    */


  /*
  * 在离线算法中，已经预先将电影的相似度矩阵进行了计算，
  * 所以每个电影movieId 的最相似的 K 个电影很容易获取：
  * 从 MongoDB 中读取 MovieRecs 数据,
  * 从 movieId 在 simHash 对应的子哈希表中获取相似度前 K 大的那些电影。
  * 输出是数据类型为 Array[Int]的数组，表示与 movieId 最相似的电影集合，
  * 并命名为candidateMovies 以作为候选电影集合。
  * */
  def getTopSimMovies(num: Int, mid: Int, uid: Int, simMovies: scala.collection.Map[Int, scala
  .collection.immutable.Map[Int,Double]])(implicit mongoConfig: MongoConfig) : Array[Int] = {
    //从广播变量的电影相似度矩阵中获取当前电影所有的相似电影
    val allSimMovies = simMovies.get(mid).get.toArray

    //获取用户已经观看过的电影
    val ratingExist: Array[Int] = ConnHander.mongoClient(mongoConfig.db)(MONGODB_RATING_COLLECTION).find(MongoDBObject
    ("uid" -> uid)).toArray.map {
      item => item.get("mid").toString.toInt
    }

    //过滤掉已经评分过的电影，并排序输出
    allSimMovies.filter( x => !ratingExist.contains(x._1)).sortWith(_._2>_._2).take(num).map(x=>x._1)
  }

  /**
    * 计算待选电影的推荐分数
    * @param simMovies            电影相似度矩阵
    * @param userRecentlyRatings  用户最近的 k 次评分
    * @param topSimMovies         当前电影最相似的 K 个电影
    * @return
    */
  def computeMovieScores(simMovies:scala.collection.Map[Int,scala.collection.immutable.Map[Int,Double]],
                         userRecentlyRatings:Array[(Int,Double)],topSimMovies: Array[Int]):
  Array[(Int,Double)] ={
    // 用于保存每一个待选电影和最近评分的每一个电影的权重得分
    val score = scala.collection.mutable.ArrayBuffer[(Int,Double)]()

    // 用于保存每一个电影的增强因子数
    val increMap = scala.collection.mutable.HashMap[Int,Int]()

    // 用于保存每一个电影的减弱因子数
    val decreMap = scala.collection.mutable.HashMap[Int,Int]()


    for (topSimMovie <- topSimMovies;userRecentlyRating <- userRecentlyRatings){
      val simScore = getMoviesSimScore(simMovies,userRecentlyRating._1,topSimMovie)
      if (simScore > 0.6){
        score+=((topSimMovie,simScore*userRecentlyRating._2))
        if (userRecentlyRating._2>3){
          increMap.getOrDefault(topSimMovie,0) +1
        }else{
          decreMap.getOrDefault(topSimMovie,0) +1
        }
      }
    }

    score.groupBy(_._1).map{
      case (mid,sims) =>(mid,sims.map(_._2).sum/sims.length+log(increMap.getOrDefault(mid,1))-log
      (decreMap.getOrDefault(mid,1)))
    }.toArray.sortWith(_._2>_._2)

  }

  /**
    * 获取当个电影之间的相似度
    * @param simMovies       电影相似度矩阵
    * @param userRatingMovie 用户已经评分的电影
    * @param topSimMovie     候选电影
    * @return
    */
  def getMoviesSimScore(simMovies:scala.collection.Map[Int,scala.collection.immutable.Map[Int,Double]],
                         userRatingMovie:Int, topSimMovie:Int): Double ={
    simMovies.get(topSimMovie) match {
      case Some(sim) => sim.get(userRatingMovie) match {
        case Some(score) => score
        case None => 0.0
      }
      case None => 0.0
    }
  }

  // 取 10的对数
  def log(m:Int):Double ={
    math.log(m) / math.log(10)
  }

  /**
    * 将数据保存到 MongoDB    uid -> 1,  recs -> 22:4.5|45:3.8
    * @param streamRecs  流式的推荐结果
    * @param mongConfig  MongoDB 的配置
    */
  def saveRecsToMongoDB(uid:Int,streamRecs:Array[(Int,Double)])(implicit mongConfig:MongoConfig)
  : Unit ={
    // 到 StreamRecs的连接
    val streaRecsCollection = ConnHander.mongoClient(mongConfig.db)(MONGODB_STREAM_RECS_COLLECTION)

    streaRecsCollection.findAndRemove(MongoDBObject("uid" -> uid))
    streaRecsCollection.insert(MongoDBObject("uid" -> uid, "recs" -> streamRecs.map(
      x => MongoDBObject("mid"->x._1,"score"->x._2)) ))
  }

  def main(args: Array[String]): Unit = {

    val config = Map(
      "spark.cores" -> "local[*]",
      "mongo.uri" -> "mongodb://localhost:27017/recommender",
      "mongo.db" -> "recommender",
      "kafka.topic" -> "recommender"
    )

    // 创建一个 SparkConf配置
    val sparkConf = new SparkConf().setAppName("StreamingRecommender").setMaster(config("spark.cores"))
    val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

    val sc: SparkContext = spark.sparkContext
    val ssc = new StreamingContext(sc,Seconds(2))

    implicit val mongoConfig = MongoConfig(config("mongo.uri"),config("mongo.db"))

    import spark.implicits._

    //广播电影相似矩阵
    //转换成Map[Int,Map[Int,Doubole]]
    val simMoviesMatrix: collection.Map[Int, Map[Int, Double]] = spark.read
      .option("uri", config("mongo.uri"))
      .option("collection", MONGODB_MOVIE_RECS_COLLECTION)
      .format("com.mongodb.spark.sql")
      .load()
      .as[MovieRecs]
      .rdd
      .map {
        recs => (recs.mid, recs.recs.map(x => (x.mid, x.score)).toMap)
      }.collectAsMap()
    simMoviesMatrix
    
    val simMoviesMatrixBroadCast: Broadcast[collection.Map[Int, Map[Int, Double]]] = sc.broadcast(simMoviesMatrix)

    //创建到kafka的连接
    val kafkaPara = Map(
      "bootstarp.servers"->"localhost:9092",
      "key.deserializer"->classOf[StringDeserializer],
      "value.deserializer"->classOf[StringDeserializer],
      "group.id"->"recommender",
      "auto.offset.reset"->"latest"
    )

    val kafkaStream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](ssc, LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](Array(config("kafka.topic")), kafkaPara))
    kafkaStream

    //UID|MOD|SCORE|TIMESTAMP
    //产生评分流
    val ratingStream: DStream[(Int, Int, Double, Int)] = kafkaStream.map {
      case msg =>
        var attr = msg.value().split("\\|")
        (attr(0).toInt, attr(1).toInt, attr(2).toDouble, attr(3).toInt)
    }

    //核心实时推荐算法
    ratingStream.foreachRDD{
      rdd=>rdd.map{
        case (uid,mid,score,timstamp)=>
          print(">>>>>>>>>>>>>>>")

        //获取当前最近的M次电影评分
          val userRecentlyRatings = getUserRecentlyRating(MAX_USER_RATINGS_NUM,uid,ConnHander.jedis)

        //获取电影p最相似的k个电影
        val simMovies = getTopSimMovies(MAX_SIM_MOVIES_NUM,mid,uid,simMoviesMatrixBroadCast.value)

        //计算待选电影的推荐优先级
        val streamRecs = computeMovieScores(simMoviesMatrixBroadCast.value,userRecentlyRatings,
          simMovies)

        //将数据保存到MongoDB
        saveRecsToMongoDB(uid,streamRecs)
      }.count()
    }

    //启动stream程序
    ssc.start()
    ssc.awaitTermination()
  }

}
