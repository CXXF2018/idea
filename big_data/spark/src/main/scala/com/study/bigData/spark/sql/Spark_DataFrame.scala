package com.study.bigData.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

object Spark_DataFrame {

  def main(args: Array[String]): Unit = {
    //SparkSQL

    //SparkConf
    //创建配置对象
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("Spark_sql")

    //SparkContext
    //SparkSession
    //创建SparkSQL的环境对象
//    val spark = new SparkSession(sparkConf)//私有的，这种访问的方式失败了
    val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

    //读取数据，构建DataFrame
    val frame: DataFrame = spark.read.json("input/user.json")

    //展示数据
    frame.show()

    //释放资源
    spark.stop()

  }

}
