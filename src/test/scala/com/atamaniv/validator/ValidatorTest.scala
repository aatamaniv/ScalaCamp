package com.atamaniv.validator


import com.atamaniv.validator.Validator.{lessThan, positiveInt, isPersonValid}
import ValidApp._
import org.scalatest.FlatSpec


class ValidatorTest extends FlatSpec {

  "Validator" should "validate positive int" in {
    val posInt = 44
    val negativeInt = -23

    val positiveRes = posInt validate positiveInt
    val negativeRes = negativeInt validate positiveInt

    assert(positiveRes.isRight)
    assert(positiveRes.right.get == posInt)
    assert(negativeRes.isLeft)
    assert(negativeRes.left.get == s"$negativeInt is not a positive int")
  }

  "Validator" should "validate lessThen" in {

    val positiveRes = 44 validate lessThan(45)
    val negativeRes = 44 validate lessThan(3)

    assert(positiveRes.isRight)
    assert(positiveRes.right.get == 44)
    assert(negativeRes.isLeft)
    assert(negativeRes.left.get == "44 is not less then 3")
  }

  "Validator" should "validate positive int AND lessThen" in {

    val positiveRes = 44 validate (positiveInt and lessThan(45))
    val positiveButNotLessThen = 44 validate (positiveInt and lessThan(3))
    val negativeIntAndNotLessThen = -5 validate (positiveInt and lessThan(-25))

    assert(positiveRes.isRight)
    assert(positiveRes.right.get == 44)
    assert(positiveButNotLessThen.isLeft)
    assert(positiveButNotLessThen.left.get == "44 is not less then 3")
    assert(negativeIntAndNotLessThen.isLeft)
    assert(negativeIntAndNotLessThen.left.get == "-5 is not a positive int -5 is not less then -25")
  }

  "Validator" should "validate positive int OR lessThen" in {

    val positiveRes = 44 validate (positiveInt or lessThan(45))
    val positiveButNotLessThen = 44 validate (positiveInt or lessThan(3))
    val negativeIntAndNotLessThen = -5 validate (positiveInt or lessThan(-25))

    assert(positiveRes.isRight)
    assert(positiveRes.right.get == 44)
    assert(positiveButNotLessThen.isRight)
    assert(positiveButNotLessThen.right.get == 44)
    assert(negativeIntAndNotLessThen.isLeft)
    assert(negativeIntAndNotLessThen.left.get == "-5 is not a positive int -5 is not less then -25")
  }

  "Validator" should "validate Person" in {

    val validPerson = Person("John Snow", 2)
    val invalidPerson = Person("", 5)
    val intValidPerson2 = Person("", 0)

    val validRes = validPerson validate isPersonValid
    val invalidRes = invalidPerson validate isPersonValid
    val invalidRes2 = intValidPerson2 validate isPersonValid

    assert(validRes.isRight)
    assert(validRes.right.get == validPerson)
    assert(invalidRes.isLeft)
    assert(invalidRes.left.get == "Person data is not valid")
    assert(invalidRes2.isLeft)
    assert(invalidRes2.left.get == "Person data is not valid")
  }

}
