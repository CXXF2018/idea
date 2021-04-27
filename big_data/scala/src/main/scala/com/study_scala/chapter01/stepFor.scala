package com.study_scala.chapter01

object stepFor {
  def main(args: Array[String]): Unit = {
    for(i<-1 to 10){
      println(i)
    }

    for(i<-Range(1,10,2)){
      println(i)
    }

    for (i<-1 to 10 if i%2==1){
      println(i)
    }
  }

}
