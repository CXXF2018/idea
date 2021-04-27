package com.study.bigData.spark.streaming

import java.io.{BufferedReader, InputStreamReader}
import java.net.Socket

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.receiver.Receiver
import org.apache.spark.streaming.{Seconds, StreamingContext}

//自定义采集器
object SparkStream_KafkaSource {
  def main(args: Array[String]): Unit = {
    //用sparkStreaming完成WordCount

    //spark配置对象
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("wordCount")

    //实时数据分析的换进对象streamingContext
    //采集周期：以指定的时间为周期采集实时数据
    val streamingContext = new StreamingContext(sparkConf,Seconds(3))

    //从kafka采集数据
   val kafkaDStream: ReceiverInputDStream[(String, String)] = KafkaUtils.createStream(streamingContext,"hadoop01:2181","sparkTest",Map("sparkTest"->3))

    //分解采集的数据(kafka发送的消息是k-v对）
    val wordDStream: DStream[String] = kafkaDStream.flatMap(t=>t._2.split(" "))

    //转变数据结构，方便统计分析
    val mapDStream: DStream[(String, Int)] = wordDStream.map((_,1))

    //将转换结构后的数据进行聚合处理
    val wordToSumDStream: DStream[(String, Int)] = mapDStream.reduceByKey(_+_)

    //打印结果
    wordToSumDStream.print()

    //此时程序执行一次就结束了，因而需要加入以下代码

    //启动采集器
    streamingContext.start()

    //Driver等待采集器的执行
    streamingContext.awaitTermination()
  }

}

