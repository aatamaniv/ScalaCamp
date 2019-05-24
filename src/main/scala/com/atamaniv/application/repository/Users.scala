package com.atamaniv.application.repository

import com.atamaniv.application.model.User
import slick.jdbc.H2Profile.api._

class Users(tag: Tag) extends Table[User](tag, "users") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def username = column[String]("username")

  def address = column[Option[String]]("address")

  def email = column[String]("email")

  def * = (id, username, address, email) <> (User.tupled, User.unapply)
}