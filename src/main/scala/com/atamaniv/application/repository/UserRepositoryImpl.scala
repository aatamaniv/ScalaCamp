package com.atamaniv.application.repository

import com.atamaniv.application.model.User
import slick.jdbc.H2Profile.api._

import scala.concurrent.Future


class UserRepositoryImpl extends UserRepository[Future] {

  val db = Database.forConfig("h2mem1")

  val users = TableQuery[Users]

  def createSchema =  db.run(users.schema.create)


  override def registerUser(user: User): Future[Int] = {
    db.run(users += user)
  }

  override def getById(id: Long): Future[Option[User]] = getUserById(id)

  override def getByUserName(username: String): Future[Option[User]] =
    db.run(users.filter(_.username === username).result.headOption)

  def getUserById(id: Long): Future[Option[User]] =
    db.run(users.filter(_.id === id).result.headOption)


  def connectToDB(): Unit = {
    val db = Database.forConfig("h2mem1")
    try {
      //TODO: do something here
    } finally db.close
  }
}