package com.study_scala.chapter01

object UnitNULLNothing {
  def main(args: Array[String]): Unit = {
    val res=sayHello()
    println(res)

    val dog:Dog=null
    println(dog)

  }

  def sayHello(): Unit ={

  }
}

class Dog{

}
