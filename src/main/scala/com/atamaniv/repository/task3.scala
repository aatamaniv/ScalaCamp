package com.atamaniv.repository

import java.util.concurrent.atomic.AtomicLong

import cats.{Id, Monad}

import scala.concurrent.Future

/**
  * Repository and Service implementation using tagless final pattern.
  * The idea is to make it easier to test our database layer, using Scala’s higher kinded types to abstract
  * the Future type constructor away from our traits under test.
  * Intro to tagless final: https://www.basementcrowd.com/2019/01/17/an-introduction-to-tagless-final-in-scala/.
  * The similar task example https://github.com/LvivScalaClub/cats-playground/blob/master/src/main/scala/BookRepository.scala
  */

case class User(id: Long, username: String)

case class IotDevice(id: Long, userId: Long, sn: String)

// NOTE: This import bring into the scope implicits that allow you to call .map and .flatMap on the type F[_]
// and also bring you typeclasses that know how to flatmap (Monad) and map (Functor) over your higher-kinded type.
import cats.implicits._

trait UserRepository[F[_]] {
  def registerUser(username: String): F[User]

  def getById(id: Long): F[Option[User]]

  def getByUsername(username: String): F[Option[User]]
}

trait IotDeviceRepository[F[_]] {
  def registerDevice(userId: Long, serialNumber: String): F[IotDevice]

  def getById(id: Long): F[Option[IotDevice]]

  def getBySn(sn: String): F[Option[IotDevice]]

  def getByUser(userId: Long): F[Seq[IotDevice]]
}

class UserService[F[_]](repository: UserRepository[F])
                       (implicit monad: Monad[F]) {

  def registerUser(username: String): F[Either[String, User]] = {
    // .flatMap syntax works because of import cats.implicits._
    // so flatMap function is added to F[_] through implicit conversions
    // The implicit monad param knows how to flatmap and map over your F.
    repository.getByUsername(username).flatMap({
      case Some(user) =>
        monad.pure(Left(s"User $user already exists"))
      case None =>
        // .map syntax works because of import cats.implicits._
        // so map function is added to F[_] through implicit conversions
        repository.registerUser(username).map(Right(_))
    })
  }

  def getByUsername(username: String): F[Option[User]] = repository.getByUsername(username)

  def getById(id: Long): F[Option[User]] = repository.getById(id)
}

class IotDeviceService[F[_]](repository: IotDeviceRepository[F],
                             userRepository: UserRepository[F])
                            (implicit monad: Monad[F]) {
  def registerDevice(userId: Long, sn: String): F[Either[String, IotDevice]] = {
    userRepository.getById(userId).flatMap({
      case None =>
        monad.pure(Left(s"User with id $userId does not exist"))
      case Some(device) =>
        repository
          .getBySn(sn)
          .flatMap({
            case Some(_) => monad.pure(Left(s"Device with sn '$sn' is already registered, it has id: ${device.id}"))
            case None => repository.registerDevice(userId, sn).map(Right(_))
          })
    })
  }
}

/** *
  * TASK 1 Start
  */

class UserRepositoryInMemory extends UserRepository[Id] {
  private var users: Map[Long, User] = Map()
  private var lastId: Long = 0L

  override def registerUser(username: String): Id[User] = {
    val id = lastId + 1
    lastId = id
    val user = User(id, username)
    users = users + (user.id -> user)
    user
  }

  override def getById(id: Long): Id[Option[User]] = users.get(id)

  override def getByUsername(username: String): Id[Option[User]] = users.values.find(_.username == username)

}

class IotDeviceRepositoryInMemory extends IotDeviceRepository[Id] {
  private var devices: Map[Long, IotDevice] = Map()
  private var lastId: AtomicLong = new AtomicLong(0L)

  override def registerDevice(userId: Long, serialNumber: String): Id[IotDevice] = {
    val device = IotDevice(lastId.addAndGet(1), userId, serialNumber)
    devices = devices + (device.id -> device)
    device
  }

  override def getById(id: Long): Id[Option[IotDevice]] = devices.get(id)

  override def getBySn(sn: String): Id[Option[IotDevice]] = devices.values.find(_.sn == sn)

  override def getByUser(userId: Long): Id[Seq[IotDevice]] = devices.values.filter(_.userId == userId).toList
}

/** *
  * TASK 1 End
  */


/** *
  * TASK 2 Start
  */


class IotDeviceRepositoryFuture extends IotDeviceRepository[Future] {
  private var devices: Map[Long, IotDevice] = Map()
  private var lastId: Long = 0L

  override def registerDevice(userId: Long, serialNumber: String): Future[IotDevice] = {
    Future.successful {
      val id = lastId + 1
      lastId = id
      val device = IotDevice(id, userId, serialNumber)
      devices = devices + (device.id -> device)
      device
    }
  }

  override def getById(id: Long): Future[Option[IotDevice]] = Future.successful(devices.get(id))

  override def getBySn(sn: String): Future[Option[IotDevice]] = Future.successful(devices.values.find(_.sn == sn))

  override def getByUser(userId: Long): Future[Seq[IotDevice]] = Future.successful(devices.values.filter(_.userId == userId).toList)
}

/** *
  * TASK 2 End
  */

/**
  * Task 3 Start
  **/


object MonadApp extends App {
  val userRepository = new UserRepositoryInMemory
  val userService = new UserService[Id](userRepository)

  val firstTime = userService.registerUser("User1")
  val secondTime = userService.registerUser("User1")

  assert(firstTime.isRight)
  assert(secondTime.isLeft)
}

// task1: implement in-memory Respository with Id monad.
// task2: implement in-memory Respository with Future monad
// example https://github.com/LvivScalaClub/cats-playground/blob/master/src/main/scala/BookRepository.scala

// task3: unit tests