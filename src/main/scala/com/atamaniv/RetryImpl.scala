package com.atamaniv

import scala.annotation.tailrec
import scala.concurrent.duration.FiniteDuration

object RetryImpl {

  @tailrec
  final def retry[A](block: () => A, acceptResult: A => Boolean, retries: List[FiniteDuration]): A = {
    val result = block()
    Some(result).find(acceptResult) match {
      case Some(correctResult) => correctResult
      case _ => retries match {
        case Nil => result
        case head :: tail =>
          sleepFor(head.toMillis)
          retry(block, acceptResult, tail)
      }
    }
  }

  private def sleepFor(mills: Long): Unit = {
    Thread.sleep(mills)
  }
}