package com.study_scala.chapter05.mylazy

object LazyDemo01 {
  def main(args: Array[String]): Unit = {
    lazy val res=sum(12,12)

    println("dasdddddddddddddddddddddd")
    println(res)
  }

  def sum(n1:Int,n2:Int):Int={
    println("执行了")
    return n1+n2
  }
}
