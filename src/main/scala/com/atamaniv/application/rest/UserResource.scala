package com.atamaniv.application.rest

import akka.http.scaladsl.server.Directives._
import com.atamaniv.application.model.User
import com.atamaniv.application.repository.UserRepositoryImpl

import scala.concurrent.Future

object UserResource {

  val repository = new UserRepositoryImpl

  val routes =
    path("user") {
      get {
        parameters('id) { userId =>
          complete(s"Requested $userId")
        }
      } ~
        post {
          entity(as[User]) { user =>
            val saved: Future[User] = repository.registerUser(user)
            onComplete(saved) { _ =>
              complete("user created")
            }
          }
        }
    }
}
