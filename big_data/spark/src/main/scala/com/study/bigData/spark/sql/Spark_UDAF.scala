package com.study.bigData.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, DoubleType, LongType, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object Spark_UDAF {

  def main(args: Array[String]): Unit = {
    //SparkSQL

    //SparkConf
    //创建配置对象
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("Spark_sql")

    //SparkContext
    //SparkSession
    //创建SparkSQL的环境对象
    val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

    //转换前需要引入隐式转换规则
    import spark.implicits._

    //自定义聚合函数
    //创建聚合函数对象
    val udaf = new MyAgeAvgFunction

    //注册聚合函数
    spark.udf.register("avgAge",udaf)

    //读取数据，构建DataFrame
    val frame: DataFrame = spark.read.json("input/user.json")

    //使用聚合函数
    frame.createTempView("User")

    spark.sql("select avgAge(age) from User").show()

    //关闭资源
    spark.stop()
  }

}

//声明用户自定义聚合函数
//1）继承UserDefinedAggregateFunction
//2）实现方法
class MyAgeAvgFunction extends UserDefinedAggregateFunction{

  //函数输入的数据结构
  override def inputSchema: StructType = {
    new StructType().add("age",LongType)
  }

  //计算时的数据结构（缓冲区的数据结构）
  override def bufferSchema: StructType = {
    new StructType().add("sum",LongType).add("count",LongType)
  }

  //函数返回的数据类型
  override def dataType: DataType = DoubleType

  //函数是否稳定性
  override def deterministic: Boolean = true

  //函数计算之前缓冲区的初始化
  override def initialize(buffer: MutableAggregationBuffer): Unit = {
    buffer(0)=0L//sum的初始值
    buffer(1)=0L//count的初始值
  }

  //根据查询结果更新数据
  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    buffer(0)=buffer.getLong(0)+input.getLong(0)
    buffer(1)=buffer.getLong(1)+1
  }

  //将多个节点的缓冲区合并
  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    //sum
    buffer1(0)=buffer1.getLong(0)+buffer2.getLong(0)
    //count
    buffer1(1)=buffer1.getLong(1)+buffer2.getLong(1)
  }

  //计算
  override def evaluate(buffer: Row): Any = {
    buffer.getLong(0).toDouble/buffer.getLong(1)
  }

}

