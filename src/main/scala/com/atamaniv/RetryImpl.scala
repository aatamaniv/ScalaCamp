package com.atamaniv

import scala.annotation.tailrec
import scala.concurrent.duration.FiniteDuration

class RetryImpl extends Retry {

  private def sleepFor(mills: Long): Unit = {
    Thread.sleep(mills)
  }

  @tailrec
  override final def retry[A](block: () => A, acceptResult: A => Boolean, retries: List[FiniteDuration]): A = {
    val result = block()

    if (acceptResult(result))
      result
    else retries match {
      case head :: Nil =>
        sleepFor(head.toMillis)
        retry(block, acceptResult, Nil)
      case head :: tail => sleepFor(head.toMillis)
        retry(block, acceptResult, tail)
      case Nil => throw new Throwable("Cannot retry any more")
    }
  }
}