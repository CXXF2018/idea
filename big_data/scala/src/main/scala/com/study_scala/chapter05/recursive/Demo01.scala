package com.study_scala.chapter05.recursive

object Demo01 {

  def main(args: Array[String]): Unit = {
    println(fbn(7))
    println(f(8))
  }

  def fbn(n:Int): Int={
    if (n==1||n==2){
      1
    }else{
      fbn(n-1)+fbn(n-2)
    }
  }

  def f(n:Int): Int={
    if (n==1){
      3
    }else{
      2*f(n-1)
    }
  }

}
