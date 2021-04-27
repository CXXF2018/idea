package com.study_scala.chapter05.details

object Details {
  def main(args: Array[String]): Unit = {
    val tiger = new Tiger
    test01(1,tiger)
    println(tiger.name)
  }

  def test01(n1:Int,tiger:Tiger): Tiger={
    println("n1=" + n1)
    tiger.name="jack"
    tiger
  }

}
class Tiger{
  var name=""
}