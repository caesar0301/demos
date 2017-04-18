package io.transwarp.demo

import java.util
import java.util.Map.Entry

import scala.collection.JavaConversions._

/**
  * Created by chenxm on 17-4-5.
  */
object TestFlatmap {

  def main(args: Array[String]): Unit = {
    val jlist = new util.ArrayList[util.HashMap[String, Int]]()

    def createHm(): util.HashMap[String, Int] = {
      val map = new util.HashMap[String, Int]()
      map.put("hello", 1)
      map.put("world", 100)
      map
    }

    jlist.add(createHm())
    jlist.add(createHm())

    val c = jlist.flatMap(_.entrySet()).map {
      case e: Entry[String, Int] =>
        val a = (e.getKey, e.getValue)
        a
    }

    c.toIterator.foreach { case i: (String, Int) =>
      println(i._1 + ": " + i._2)
    }
  }
}
