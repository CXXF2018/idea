package com.study.bigData.spark.Oper01

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark_Oper08 {
  def main(args: Array[String]): Unit = {
    //TODO 1 准备spark环境
    //local[*]: 这种模式直接帮你按照Cpu最多Cores来设置线程数了
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("wordCount")

    //TODO 2 建立与Spark的连接
    val sc = new SparkContext(sparkConf)

    //TODO  3 使用makeRDD
    val listRDD: RDD[Int] = sc.makeRDD(1 to 10)//此处可以设置分区数（1 to 10,2)

   val sampleRDD: RDD[Int] = listRDD.sample(false,0.6,1)
    //第三个参数是随机数种子，一种假的随机，种子相同时产生的序列相同
//    第二个参数位于0-1之间
    //第一个参数为true时放回去，为false则抽取后不放回去
    sampleRDD.collect().foreach(println)
  }

}
