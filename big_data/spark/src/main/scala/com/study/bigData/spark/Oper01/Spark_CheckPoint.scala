package com.study.bigData.spark.Oper01

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark_CheckPoint {
  def main(args: Array[String]): Unit = {
    //TODO 1 准备spark环境
    //local[*]: 这种模式直接帮你按照Cpu最多Cores来设置线程数了
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("wordCount")
    //TODO 2 建立与Spark的连接
    val sc = new SparkContext(sparkConf)

    //TODO  设置检查点的保存目录
    sc.setCheckpointDir("cp")

    //TODO  3 使用makeRDD
    val listRDD: RDD[Int] = sc.makeRDD(1 to 10)//此处可以设置分区数（1 to 10,2)

    val mapRDD: RDD[(Int, Int)] = listRDD.map((_,1))

    mapRDD.checkpoint()

    val reduceRDD: RDD[(Int, Int)] = mapRDD.reduceByKey(_+_)
    reduceRDD.checkpoint()

    reduceRDD.foreach(println)

    println(reduceRDD.toDebugString)

  }

}
