package io.transwarp.demo.closure

case class FakeObj(name: String)

class Closure1 {

  val a = "a"
  val b = "I'm a large object !!!!!!!!!!!!!!!!!!!!!!!!!!"

  def closure(arg: Array[Int]): Unit = {
    arg.foreach(n => {
      println("%s : %s".format(n, a))
    })
  }
}

class Closure2 {

  val a = "a"
  val b = "I'm a large object !!!!!!!!!!!!!!!!!!!!!!!!"
  val c = FakeObj("i am fake")

  def closure(arg: Array[Int]): Unit = {
    val aa = a
    val cc = c
    arg.foreach(n => {
      println("%s : %s %s".format(n, aa, cc))
    })
  }
}

object Closure {

  val array = (0 until 1).toArray

  def main(args: Array[String]): Unit = {
    new Closure1().closure(array)
    new Closure2().closure(array)
  }
}