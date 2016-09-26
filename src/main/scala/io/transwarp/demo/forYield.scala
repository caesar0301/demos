package io.transwarp.demo

import java.util

/**
  * Created by chenxm on 16-6-17.
  */
object forYield {

  def main(args: Array[String]): Unit = {
    var dat = new util.LinkedList[(String, Seq[Int])]
    val par1 = Seq(1,2,3)
    val par2 = Seq(4,5,6)
    dat.add("a" -> par1)
    dat.add("b" -> par2)

    for {
      (key, vv) <- dat.toArray.toSeq
      v: Int <-  vv.asInstanceOf[Seq[Int]]
    } yield {
      println(v)
    }
  }

}
