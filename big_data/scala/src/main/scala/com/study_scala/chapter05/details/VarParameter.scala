package com.study_scala.chapter05.details

object VarParameter {
  def main(args: Array[String]): Unit = {
    println(sum(10, 20, 10, 21, 321, 312, 31, 2, 2313, 12))
  }

  def sum(n1:Int,args:Int*):Int={
    println(args.length)
    var sum=n1
    for(item<-args){
      sum+=item
    }
    sum
  }

}
