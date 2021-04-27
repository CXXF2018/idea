package com.study.bigData.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object Spark_Transform1 {

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

    //创建RDD
    val rdd: RDD[(Int, String, Int)] = spark.sparkContext.makeRDD(List((1, "zhangsan", 10), (1, "s", 20), (2, "c", 30),
      (3, "dd", 40), (4, "wref", 50)))
    rdd

    //转换前需要引入隐式转换规则
    import spark. implicits._

    //RDD--DataSet
    val userRDD: RDD[User] = rdd.map {
      case (id, name, age) => {
        User(id, name, age)
      }
    }
    val userDS: Dataset[User] = userRDD.toDS()

    val rdd1: RDD[User] = userDS.rdd

    rdd1.foreach(println)

    //关闭资源
    spark.stop()
  }

}

