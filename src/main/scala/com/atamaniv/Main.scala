package com.atamaniv

import java.util.concurrent.TimeUnit

import scala.concurrent.duration.FiniteDuration
import scala.util.Random

object Main extends App {

  override def main(args: Array[String]): Unit = {
    val isAcceptable: Int => Boolean = result => result > 5
    val retryObject = new RetryImpl
    val toPrint = retryObject.retry(() => Random.nextInt(10),
      isAcceptable,
      List(
        FiniteDuration(1000, TimeUnit.MILLISECONDS),
        FiniteDuration(2000, TimeUnit.MILLISECONDS),
        FiniteDuration(3000, TimeUnit.MILLISECONDS))
    )

    println(s"Result: $toPrint")
  }

}