package com.study.bigData.spark.Oper01

import java.util

import org.apache.spark.rdd.RDD
import org.apache.spark.util.{AccumulatorV2, LongAccumulator}
import org.apache.spark.{SparkConf, SparkContext}

object Spark_shareData {
  def main(args: Array[String]): Unit = {
    //TODO 1 准备spark环境
    //local[*]: 这种模式直接帮你按照Cpu最多Cores来设置线程数了
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("wordCount")

    //TODO 2 建立与Spark的连接
    val sc = new SparkContext(sparkConf)

    //TODO  3 使用makeRDD
    val listRDD: RDD[String] = sc.makeRDD(List("hadoop","hive","hbase","Scala","spark"),2)
    //此处可以设置分区数（1
    // to
    // 10,2)
/*
    var sum:Int=0
    /*
    var sum:Int=0
    listRDD.foreach(i=>sum=sum+i)
    println("sum="+sum)
    直接这样得出的结果为0，因为这个过程在executor中执行，且结果没有回收
    */

    //使用累加器来共享变量，累加数据

    //创建累加对象
    val accumulator: LongAccumulator = sc.longAccumulator

    listRDD.foreach{
      case i=>{
        //执行累加功能
          accumulator.add(i)
      }
    }
    println("sum="+accumulator.value)*/

    //TODO 创建累加器
    var wordAccumulator=new WordAccumulator
    //TODO 注册累加器
    sc.register(wordAccumulator)

    listRDD.foreach{
      case word=>{
        //执行累加功能
        wordAccumulator.add(word)
      }
    }

    println("sum=" + wordAccumulator.value)

    sc.stop()
  }

  //声明累加器
  //继承AccumulaotrV2
  //实现抽象方法
  class WordAccumulator extends AccumulatorV2[String,util.ArrayList[String]]{

    private val list = new util.ArrayList[String]()

    //当前累加器是否为初始化状态
    override def isZero: Boolean = {
      list.isEmpty
    }

    //复制累加器对象
    override def copy(): AccumulatorV2[String, util.ArrayList[String]] = {
      new WordAccumulator
    }

    //重置累加器对象
    override def reset(): Unit = {
      list.clear()
    }

    //向累加器中增加数据
    override def add(v: String): Unit = {
      if(v.contains("h")){
        list.add(v)
      }
    }

    //合并
    override def merge(other: AccumulatorV2[String, util.ArrayList[String]]): Unit = {
      list.addAll(other.value)
    }

    //获取累加器的结果
    override def value: util.ArrayList[String] = list
  }
}
