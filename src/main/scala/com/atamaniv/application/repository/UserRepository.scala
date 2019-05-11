package com.atamaniv.application.repository

import com.atamaniv.application.model.User

trait UserRepository[F[_]] {
  def registerUser(user: User): F[User]

  def getById(id: Long): F[Option[User]]

  def getByUserName(username: String): F[Option[User]]
}