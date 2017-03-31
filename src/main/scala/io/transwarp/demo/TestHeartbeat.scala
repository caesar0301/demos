package io.transwarp.demo

import java.util.concurrent.TimeoutException
import java.util.{Timer, TimerTask}

/**
  * Created by chenxm on 17-3-30.
  */
object TestHeartbeat {

  var checkpoint = -1L

  val task = new TimerTask {
    override def run(): Unit = {
      if (checkpoint < 0)
        checkpoint = System.currentTimeMillis()
      else if (System.currentTimeMillis() - checkpoint > 3000) {
        throw new TimeoutException("Time out")
        // println("Timeout")
      } else {
        println("Normal")
      }
    }
  }
  val timer = new Timer()
  timer.schedule(task, 0, 1000)

  def heartbeat() = {
    checkpoint = System.currentTimeMillis()
  }

  def main(args: Array[String]): Unit = {
    while(true) {
      heartbeat()
      println("Start sleeping")
      Thread.sleep(5000)
    }
  }
}
