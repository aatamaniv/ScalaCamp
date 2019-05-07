package com.atamaniv.tailrec

import org.scalatest.FlatSpec
import org.scalatest.concurrent.{ScalaFutures, TimeLimits}
import org.scalatest.time.{Seconds, Span}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

class RetryImplTest extends FlatSpec with TimeLimits with ScalaFutures {

  "A RetryImpl" should "return result of block code without retries" in {
    failAfter(Span(1, Seconds)) {
      whenReady(RetryImpl.retry(
        () => Future(Some(5)),
        (result: Int) => result > 0,
        List(5.seconds))) { res: Option[Int] =>
        assert(res.isDefined)
        assert(res.contains(5))
      }
    }
  }


  "A RetryImpl" should "return not empty result if it is acceptable and retries list is empty" in {
    whenReady(RetryImpl.retry(
      () => Future(Some(5)),
      (result: Int) => result > 0,
      Nil)) { res: Option[Int] =>
      assert(res.isDefined)
      assert(res.contains(5))
    }
  }

  "A RetryImpl" should "return None if result is not acceptable and retries list is empty" in {
    whenReady(RetryImpl.retry(
      () => Future(Some(5)),
      (result: Int) => result < 0,
      Nil)) { res: Option[Int] =>
      assert(res.isEmpty)
    }
  }


  "A RetryImpl" should "return result of block code with retries" in {
    var resultSet = Set(1, 2, 3, 4, 5)

    val intGenerator = () => {
      val toReturn = resultSet.head
      resultSet = resultSet.tail
      toReturn
    }
    val isAcceptable: Int => Boolean = result => result == 5
    val retries = List.fill(resultSet.size - 1)(10.milliseconds)

    whenReady(RetryImpl.retry(
      () => Future(Some(intGenerator())),
      isAcceptable,
      retries)) { res: Option[Int] =>
      assert(res.isDefined)
    }
  }

  "A RetryImpl" should "retry n times" in {
    var counter = 0

    val intGenerator = () => {
      val zero = 0
      counter += 1
      zero
    }
    val isAcceptable: Int => Boolean = result => result == 1
    val retries = List.fill(10000)(0.milliseconds)

    whenReady(RetryImpl.retry(
      () => Future(Some(intGenerator())),
      isAcceptable,
      retries)) { _ =>
      assert(counter == retries.size + 1)
    }
  }
}