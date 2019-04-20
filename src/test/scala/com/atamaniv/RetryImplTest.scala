package com.atamaniv

import java.util.concurrent.TimeUnit

import org.scalatest.FlatSpec
import org.scalatest.concurrent.TimeLimits
import scala.concurrent.duration._

import scala.concurrent.duration.FiniteDuration
import org.scalatest.time.{Seconds, Span}
import scala.util.Random

class RetryImplTest extends FlatSpec with TimeLimits {

  "A RetryImpl" should "return result of block code without retries" in {
    failAfter(Span(1, Seconds)) {
      assert(RetryImpl.retry(
        () => 5,
        (result: Int) => result > 0,
        List(10.seconds)) == 5)
    }
  }


  "A RetryImpl" should "return first result that is acceptable without retries" in {
    assert(RetryImpl.retry(
      () => 5,
      (result: Int) => result > 0,
      Nil) == 5)
  }

  "A RetryImpl" should "return first result even it is not acceptable without retries" in {
    assert(RetryImpl.retry(
      () => 5,
      (result: Int) => result == 100,
      Nil) == 5)
  }

  "A RetryImpl" should "return result of block code with retries" in {
    val intGenerator = () => Random.nextInt(2)
    val isAcceptable: Int => Boolean = result => result == 1
    val retries = List(
      FiniteDuration(10, TimeUnit.MILLISECONDS),
      FiniteDuration(10, TimeUnit.MILLISECONDS),
      FiniteDuration(10, TimeUnit.MILLISECONDS),
      FiniteDuration(10, TimeUnit.MILLISECONDS),
      FiniteDuration(10, TimeUnit.MILLISECONDS))

    assert(RetryImpl.retry(intGenerator, isAcceptable, retries) == 1)
  }

  "A RetryImpl" should "retry n times" in {
    var counter = 0

    val intGenerator = () => {
      val generatedNumber = Random.nextInt(100)
      counter += 1
      generatedNumber
    }
    val isAcceptable: Int => Boolean = result => result == 1
    val retries = List(
      FiniteDuration(0, TimeUnit.MILLISECONDS),
      FiniteDuration(0, TimeUnit.MILLISECONDS),
      FiniteDuration(0, TimeUnit.MILLISECONDS)
    )

    RetryImpl.retry(intGenerator, isAcceptable, retries)

    assert(counter == retries.size + 1)
  }
}