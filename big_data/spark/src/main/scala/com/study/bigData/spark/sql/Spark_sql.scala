package com.study.bigData.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

object Spark_sql {

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

    //将DataFrame转化为一张表
    frame.createTempView("user")

    //采用sql语句进行访问
    spark.sql("select * from user").show

    //释放资源
    spark.stop()

  }

}
