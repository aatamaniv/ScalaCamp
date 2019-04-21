package com.atamaniv.tailrec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

object RetryImpl {

  //Added Option here, for me it is more logical, if condition is not reached then None is returned.
  final def retry[A](block: () => Future[Option[A]], acceptResult: A => Boolean, retries: List[FiniteDuration]): Future[Option[A]] = {
    block().map(opt => opt.find(acceptResult)).flatMap {
      case Some(value) => Future.successful(Some(value))
      case None => retries match {
        case head :: tail =>
          Thread.sleep(head.toMillis)
          retry(block, acceptResult, tail)
        case Nil => Future.successful(None)
      }
    }
  }
}
