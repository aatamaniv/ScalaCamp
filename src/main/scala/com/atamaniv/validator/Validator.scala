package com.atamaniv.validator

/**
  * Implement validator typeclass that should validate arbitrary value [T].
  *
  * @tparam T the type of the value to be validated.
  */
trait Validator[T] {
  /**
    * Validates the value.
    *
    * @param value value to be validated.
    * @return Right(value) in case the value is valid, Left(message) on invalid value
    */
  def validate(value: T): Either[String, T]

  /**
    * And combinator.
    *
    * @param other validator to be combined with 'and' with this validator.
    * @return the Right(value) only in case this validator and <code>other</code> validator returns valid value,
    *         otherwise Left with error messages from both validators.
    */
  def and(other: Validator[T]): Validator[T] = {
    val that = this
    new Validator[T] {
      override def validate(value: T) = {
        that.validate(value) match {
          case Left(firstLeft) => other.validate(value) match {
            case Left(second) => Left(s"$firstLeft $second")
            case Right(_) => Left(firstLeft)
          }
          case Right(_) => other.validate(value) match {
            case Left(second) => Left(s"$second")
            case Right(right) => Right(right)
          }
        }
      }
    }
  }

  /**
    * Or combinator.
    *
    * @param other validator to be combined with 'or' with this validator.
    * @return the Right(value) only in case either this validator or <code>other</code> validator returns valid value,
    *         otherwise Left with error messages from the failed validator or from both if both failed.
    */
  def or(other: Validator[T]): Validator[T] = {
    val that = this
    new Validator[T] {
      override def validate(value: T) = {
        that.validate(value) match {
          case Right(first) => Right(first)
          case Left(firstLeft) => other.validate(value) match {
            case Right(first) => Right(first)
            case Left(secondLeft) => Left(s"$firstLeft $secondLeft")
          }
        }

      }
    }
  }
}

object Validator {

  import ValidApp.IntValidator
  import ValidApp.StringValidator

  def positiveInt: Validator[Int] = new Validator[Int] {
    override def validate(t: Int): Either[String, Int] = {
      if (t > 0) Right(t)
      else Left(s"$t is not a positive int")
    }
  }

  def lessThan(n: Int): Validator[Int] = new Validator[Int] {
    override def validate(t: Int): Either[String, Int] = {
      if (t < n) Right(t)
      else Left(s"$t is not less then $n")
    }
  }

  val nonEmpty: Validator[String] = new Validator[String] {
    override def validate(t: String): Either[String, String] = {
      if (t.nonEmpty) Right(t)
      else Left(s"$t is empty")
    }
  }

  val isPersonValid = new Validator[Person] {
    // implement me
    // Returns valid only when the name is not empty and age is in range [1-99].
    override def validate(value: Person): Either[String, Person] = {
      val ageValidation: Either[String, Int] = value.age validate (positiveInt and lessThan(99))
      val nameValidation = value.name validate nonEmpty

      (ageValidation, nameValidation) match {
        case (Right(_), Right(_)) => Right(value)
        case _ => Left(s"Person data is not valid")
      }
    }
  }
}

object ValidApp {

  import Validator._

  implicit class IntValidator(n: Int) {
    def validate(validator: Validator[Int]): Either[String, Int] = {
      validator.validate(n)
    }
  }

  implicit class StringValidator(s: String) {
    def validate(validator: Validator[String]): Either[String, String] = {
      validator.validate(s)
    }
  }

  implicit class PersonValidator(p: Person) {
    def validate(validator: Validator[Person]): Either[String, Person] = {
      validator.validate(p)
    }
  }

  2 validate (positiveInt and lessThan(10))

  "" validate Validator.nonEmpty

  Person(name = "John", age = 25) validate isPersonValid
}

object ImplicitValidApp {

  import Validator._

  // uncomment next code and make it compilable and workable

  implicit val persVal = isPersonValid
  implicit val posInt = positiveInt
  implicit val strVal = nonEmpty

  implicit class PersonValidator(p: Person) {
    def validate(implicit validator: Validator[Person]): Either[String, Person] = {
      validator.validate(p)
    }
  }

  implicit class StringValidator(s: String) {
    def validate(implicit validator: Validator[String]): Either[String, String] = {
      validator.validate(s)
    }
  }

  implicit class IntValidator(n: Int) {
    def validate(implicit validator: Validator[Int]): Either[String, Int] = {
      validator.validate(n)
    }
  }

  Person(name = "John", age = 25) validate

  "asdasd" validate

  234.validate
}


case class Person(name: String, age: Int)