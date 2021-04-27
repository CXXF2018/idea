package com.study_scala.chapter05.exercise

import scala.io.StdIn

object Exercise01 {

  def main(args: Array[String]): Unit = {
    println("输入一个数字：")
    val n: Int = StdIn.readInt()
    print99(n)
  }

  def print99(n: Int)={
    for (i<-1 to n){
      for (j<-1 to i){
        printf("%d*%d=%d\t",i,j,j*i)
      }
      println()
    }
  }
}
