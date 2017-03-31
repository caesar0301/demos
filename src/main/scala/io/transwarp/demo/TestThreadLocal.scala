package io.transwarp.demo

import java.util.concurrent.{SynchronousQueue, TimeUnit, ThreadPoolExecutor}


object TestThreadLocal {
  def main(args: Array[String]): Unit = {
    case class Cnt(var i: Int) {
      override def toString() = i.toString
    }
    val a = new ThreadLocal[Cnt]() {
      override def initialValue(): Cnt = {
        Cnt(0)
      }
    }


    val pool = new ThreadPoolExecutor(1, 1, 1000, TimeUnit.SECONDS, new SynchronousQueue[Runnable])

    pool.execute(new Runnable() {
      override def run(): Unit = {
        val cnt = a.get
        println(cnt)
        cnt.i += 1
        println(cnt)

        // 不加这个 就会复用Cnt里面的值
        //        a.remove()
      }
    })

    Thread.sleep(1000)

    pool.execute(new Runnable() {
      override def run(): Unit = {
        println(a.get)
      }
    })

  }
}

