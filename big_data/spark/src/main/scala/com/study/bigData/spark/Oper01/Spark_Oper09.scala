package com.study.bigData.spark.Oper01

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark_Oper09 {
  def main(args: Array[String]): Unit = {
    //TODO 1 准备spark环境
    //local[*]: 这种模式直接帮你按照Cpu最多Cores来设置线程数了
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("wordCount")

    //TODO 2 建立与Spark的连接
    val sc = new SparkContext(sparkConf)

    //TODO  3 使用makeRDD
    //缩减分区数
    val listRDD: RDD[Int] = sc.makeRDD(1 to 16,4)
    println( "缩减分区前= " + listRDD. partitions.size)
    val coalesceRDD: RDD[Int] = listRDD. coalesce(3)
    //简单的合并最后的几个分区
    println( "缩减分区后= " + coalesceRDD. partitions.size)


    coalesceRDD.collect().foreach(println)
  }

}
