package com.atamaniv.application.rest

import akka.http.scaladsl.server.Directives._

object UserResource {

  val routes =
    path("user") {
      get {
        parameters('id) { userId =>
          complete(s"Requested $userId")
        }
      } ~
      post {
        complete("This is a POST request.")
      }
    }
}
