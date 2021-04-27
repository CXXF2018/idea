package com.study.bigData.spark.Oper01

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark_Oper01 {
  def main(args: Array[String]): Unit = {
    //TODO 1 准备spark环境
    val sparkConf = new SparkConf().setMaster("local").setAppName("wordCount")

    //TODO 2 建立与Spark的连接
    val sc = new SparkContext(sparkConf)

    //TODO  3 使用makeRDD
    val listRDD: RDD[Int] = sc.makeRDD(1 to 10)

    val mapRDD: RDD[Int] = listRDD.map(x=>x*2)//(_*2)

    mapRDD.collect().foreach(println)
  }

}
