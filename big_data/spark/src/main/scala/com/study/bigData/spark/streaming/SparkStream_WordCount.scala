package com.study.bigData.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkStream_WordCount {
  def main(args: Array[String]): Unit = {
    //用sparkStreaming完成WordCount

    //spark配置对象
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("wordCount")

    //实时数据分析的换进对象streamingContext
    //采集周期：以指定的时间为周期采集实时数据
    val streamingContext = new StreamingContext(sparkConf,Seconds(3))

    //从指定端口采集数据
    val socketDStream: ReceiverInputDStream[String] = streamingContext.socketTextStream("hadoop01",9999)

    //分解采集的数据
    val wordDStream: DStream[String] = socketDStream.flatMap(line=>line.split(" "))

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
