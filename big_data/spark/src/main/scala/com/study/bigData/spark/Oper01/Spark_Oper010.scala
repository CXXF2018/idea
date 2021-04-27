package com.study.bigData.spark.Oper01

import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkConf, SparkContext}

object Spark_Oper010 {
  def main(args: Array[String]): Unit = {
    //TODO 1 准备spark环境
    //local[*]: 这种模式直接帮你按照Cpu最多Cores来设置线程数了
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("wordCount")

    //TODO 2 建立与Spark的连接
    val sc = new SparkContext(sparkConf)

    //TODO  3 使用makeRDD
    val listRDD: RDD[(String, Int)] = sc.makeRDD(List(("a",1),("b",2),("c",3)))
    //此处可以设置分区数（1 to 10,2)
    val partittionRDD: RDD[(String, Int)] = listRDD.partitionBy(new MyPartitioner(3))

    partittionRDD.collect().foreach(println)
  }

}

class MyPartitioner(partitions : Int) extends Partitioner {
  override def numPartitions: Int = {
    partitions
  }

  override def getPartition(key: Any): Int = {
    1
  }
}
