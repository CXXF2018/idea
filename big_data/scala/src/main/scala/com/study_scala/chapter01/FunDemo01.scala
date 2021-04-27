package com.study_scala.chapter01

object FunDemo01 {

  def main(args: Array[String]): Unit = {
    var n1=100
    var n2=1000
    println(getRes(n1, n2, '+'))
  }

  def getRes(n1:Int,n2:Int,oper:Char)={
    if(oper=='+'){
      n1+n2
    }else{
      null
    }
  }

}
