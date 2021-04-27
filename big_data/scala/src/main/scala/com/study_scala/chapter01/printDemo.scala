package com.study_scala.chapter01

object printDemo {
  def main(args: Array[String]): Unit = {
    var name:String ="tom"
    var sal:Double =1.2
    println("hello"+sal+name)

    printf("name=%s sal=%f\n",name,sal)

    println(s"name=$name sal=${sal+1}")//注意前面有个s

  }

}
