package com.study_scala


  object fla_map {
    def main(args: Array[String]): Unit = {
      val rdd1= List(List("A","B"),List("C","D"))

      rdd1.map( i => println(i))
      println(rdd1)
      println("----------------------")
      val strings = rdd1.flatMap(f => f)
      println(strings)
      strings.foreach( i => println(i))
    }
  }
