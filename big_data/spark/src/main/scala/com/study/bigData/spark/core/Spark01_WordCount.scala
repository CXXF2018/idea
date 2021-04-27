package com.study.bigData.spark.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark01_WordCount {
  def main(args: Array[String]): Unit = {
    //todo Spark -wordCount
    //Spark是一个计算框架
    //开发人员是使用Spark框架的API实现计算功能

    //TODO 1 准备spark环境
    val sparkConf = new SparkConf().setMaster("local").setAppName("wordCount")

    //TODO 2 建立与Spark的连接
    val sparkContext = new SparkContext(sparkConf)

    //TODO 3 实现业务操作

      //TODO　３.1读取指定目录下的数据文件
      val fileRDD :RDD[String]= sparkContext.textFile("input")

    //TODO 3.2切分单词
    val wordRDD :RDD[String]= fileRDD.flatMap(_.split(" "))

    //TODO 3.3将分词后的数据进行分组
    val groupRDD: RDD[(String, Iterable[String])] = wordRDD.groupBy(word => word)

    //TODO 3.4将分组后的数据进行聚合(word,iterator)=>(word,count)
    val mapRDD: RDD[(String, Int)] = groupRDD.map {
      case (word, iter) => {
        (word, iter.size)
      }
    }
    mapRDD

    //TODO 3.5将聚合的结果采集后打印到控制台上
    val wordCountArray: Array[(String, Int)] = mapRDD.collect()
    println(wordCountArray.mkString(","))

    //TODO 4 关闭连接
    sparkContext.stop()

  }

}
