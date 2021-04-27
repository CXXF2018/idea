package com.study.bigData.spark.Oper01

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark_Oper06 {
  def main(args: Array[String]): Unit = {
    //TODO 1 准备spark环境
    //local[*]: 这种模式直接帮你按照Cpu最多Cores来设置线程数了
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("wordCount")

    //TODO 2 建立与Spark的连接
    val sc = new SparkContext(sparkConf)

    //TODO  3 使用makeRDD
    val listRDD: RDD[Int] = sc.makeRDD(1 to 10)//此处可以设置分区数（1 to 10,2)

    //按照函数的返回值进行分组
    //分组后数据形成了对偶元组（k-v），k表示分组key，v表示分组的数据集合
    val groupByRDD: RDD[(Int, Iterable[Int])] = listRDD.groupBy(x=>x%3)

    groupByRDD.collect().foreach(println)
  }

}
