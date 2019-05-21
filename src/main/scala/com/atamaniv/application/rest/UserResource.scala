package com.atamaniv.application.rest

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directives, Route}
import com.atamaniv.application.model.User
import com.atamaniv.application.repository.UserRepositoryImpl

import scala.concurrent.Future

class UserResource extends Directives with JsonSupport {

  val repository = new UserRepositoryImpl

  val routes: Route =
    path("user") {
      get {
        parameters('id) { userId =>
          complete(s"Requested $userId")
        }
      } ~
        post {
          entity(as[User]) { user =>
            val saved: Future[Int] = repository.registerUser(user)
            onComplete(saved) { _ =>
              complete("user created")
            }
          }
        }
    }
}


object UserResource {
  def apply(): UserResource = new UserResource()
}