package com.atamaniv

import scala.concurrent.duration.FiniteDuration

trait Retry {
  def retry[A](block: () => A,
            acceptResult: A => Boolean,
            retries: List[FiniteDuration]
           ): A
}
