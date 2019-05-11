package com.atamaniv.repository

import java.util.concurrent.atomic.AtomicLong

import scala.concurrent.Future

class UserRepositoryFuture extends UserRepository[Future] {
  private var users: Map[Long, User] = Map()
  private val lastId: AtomicLong = new AtomicLong(0L)

  override def registerUser(username: String): Future[User] = {
    Future.successful {
      val user = User(lastId.addAndGet(1), username)
      users = users + (user.id -> user)
      user
    }
  }

  override def getById(id: Long): Future[Option[User]] = Future.successful(users.get(id))

  override def getByUsername(username: String): Future[Option[User]] = Future.successful(users.values.find(_.username == username))
}