package com.atamaniv

import scala.annotation.tailrec
import scala.concurrent.duration.FiniteDuration

object RetryImpl {

  @tailrec
  final def retry[A](block: () => A, acceptResult: A => Boolean, retries: List[FiniteDuration]): A = {
    val result = block()

    if (acceptResult(result))
      result
    else retries match {
      case head :: tail =>
        sleepFor(head.toMillis)
        retry(block, acceptResult, tail)
      case Nil => result
    }
  }

  private def sleepFor(mills: Long): Unit = {
    Thread.sleep(mills)
  }
}