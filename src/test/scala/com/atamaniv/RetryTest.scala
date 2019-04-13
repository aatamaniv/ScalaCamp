package com.atamaniv

import java.util.concurrent.TimeUnit

import org.scalatest.FlatSpec

import scala.concurrent.duration.FiniteDuration
import scala.util.Random

class RetryTest extends FlatSpec {

  "A RetryImpl" should "return result of block code without retries" in {
    val retryImpl = new RetryImpl
    val intGenerator = () => 6
    val isAcceptable: Int => Boolean = result => result > 5

    assert(retryImpl.retry(intGenerator, isAcceptable, Nil) == 6)
  }

  //Risk of failure
  "A RetryImpl" should "return result of block code with retries" in {
    val retryImpl = new RetryImpl
    val intGenerator = () => Random.nextInt(2)
    val isAcceptable: Int => Boolean = result => result == 1
    val retries = List(
      FiniteDuration(10, TimeUnit.MILLISECONDS),
      FiniteDuration(10, TimeUnit.MILLISECONDS),
      FiniteDuration(10, TimeUnit.MILLISECONDS),
      FiniteDuration(10, TimeUnit.MILLISECONDS),
      FiniteDuration(10, TimeUnit.MILLISECONDS))

    assert(retryImpl.retry(intGenerator, isAcceptable, retries) == 1)
  }

  "A RetryImpl" should "throw an error then cannot retry execution any more" in {
    val retryImpl = new RetryImpl
    val intGenerator = () => 4
    val isAcceptable: Int => Boolean = result => result > 5

    assertThrows[Throwable] {
      retryImpl.retry(intGenerator, isAcceptable, Nil)
    }
  }
}