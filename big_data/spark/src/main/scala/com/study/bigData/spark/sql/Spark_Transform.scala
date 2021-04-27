package com.study.bigData.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object Spark_Transform {

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

    //转换为DataFrame
    val df: DataFrame = rdd.toDF("id","name","age")

    //转换为DataSet
    //转化为DS需要有样例类
    val ds: Dataset[User] = df.as[User]

    //转换为DataFrame
    val df1: DataFrame = ds.toDF()

    //转换为RDD
    val rdd1: RDD[Row] = df1.rdd

    rdd1.foreach(row=>{
      //此处数字为索引，故第一列为0
      println(row.getInt(0))
    })

    //关闭资源
    spark.stop()
  }

}

case class User(id:Int,name:String,age:Int)
