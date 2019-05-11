package com.atamaniv.application.repository

import java.util.concurrent.atomic.AtomicLong

import com.atamaniv.application.model.User
import scala.concurrent.Future

class UserRepositoryImpl extends UserRepository[Future] {
  private var users: Map[Long, User] = Map()
  private val lastId: AtomicLong = new AtomicLong(0L)

  override def registerUser(user: User): Future[User] = {
    Future.successful {
      User(lastId.addAndGet(1), user.username, user.address, user.email)
    }
  }

  override def getById(id: Long): Future[Option[User]] = Future.successful(users.get(id))

  override def getByUserName(username: String): Future[Option[User]] = Future.successful(users.values.find(_.username == username))

}