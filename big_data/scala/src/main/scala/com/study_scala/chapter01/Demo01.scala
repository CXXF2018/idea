package com.study_scala.chapter01

import scala.io.StdIn

object Demo01 {
  def main(args: Array[String]): Unit = {
    val name=StdIn.readLine()
    val age=StdIn.readInt()
    val sal=StdIn.readDouble()
    println(name+age+sal)
  }


  Cat.sayHi()
  Cat.sayHello()
}

object Cat extends AAA{
  def sayHi(): Unit ={
    println("dasdsa")
  }
}

trait AAA{
  def sayHello(): Unit ={
    println("dasdfad")
  }
}
