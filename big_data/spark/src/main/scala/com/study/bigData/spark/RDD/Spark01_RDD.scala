package com.study.bigData.spark.RDD

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark01_RDD {
  def main(args: Array[String]): Unit = {
    //TODO 1 准备spark环境
    val sparkConf = new SparkConf().setMaster("local").setAppName("wordCount")

    //TODO 2 建立与Spark的连接
    val sc = new SparkContext(sparkConf)

    //TODO 3 从集合中创建RDD

    //TODO  3-1 使用makeRDD
    val listRDD: RDD[Int] = sc.makeRDD(List(1,2,3,4))

    //val listRDD: RDD[Int] = sc.makeRDD(List(1,2,3,4),2)设置分区数

    listRDD.collect().foreach(println)

    //TODO 3-2 使用parallelize
    val ArrayRDD: RDD[Int] = sc.parallelize(Array(1,2,3,4,5,6))
    ArrayRDD.collect().foreach(println)

    //TODO 4 从外部存储中创建
    //默认情况读取项目路径，也可以读入其他路径：HDFS
    //默认文件中读取的数据都是字符串类型
    val fileRDD: RDD[String] = sc.textFile("input")

    //val fileRDD: RDD[String] = sc.textFile("input",2)

    fileRDD.collect().foreach(println)

    //TODO 5 保存文件
    fileRDD.saveAsTextFile("output")
  }

}
