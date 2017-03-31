package io.transwarp.demo

/**
  * Created by chenxm on 16-6-17.
  */
object TestFuncParam {

  class ClosureClass {
    def printResult[T](f: => T) = {
      println(f)
    }

    def printResult[T](f: String => T) = {
      println(f("HI THERE"))
    }
  }

  def main(args: Array[String]): Unit = {
    val cc = new ClosureClass
    cc.printResult("hello")
    cc.printResult{(a: String) => a}
  }

}
