//package com.atamaniv.application
//
///**
//  * In this task you will learn how to create a simple web service using Akka HTTP and Slick.
//  * This service should be able to create User in DB table, and retrieve User by ID.
//  *
//  * You can use the following Giter8 template to quickly setup a SBT project with Akka HTTP dependencies:
//  * https://github.com/akka/akka-http-quickstart-scala.g8
//**/
//
///**
//  * Task 1: Create a database table called User. Create Slick schema class, and a DB repository on top of it.
//  *
//  * Here is the model class which should correspond to DB table:
//  * case class User(id: Long, username: String, address: Option[String], email: String)
//  *
//  * Slick docs: https://slick.lightbend.com/doc/3.3.0/
//**/
//
///**
//  * Task 2: Implement two HTTP routes using Akka Http.
//  *
//  * Task 2a: [POST] Route for registering Users.
//  * POST /user
//  * Payload:
//  * - username: String (should be non-empty and contain only alphanumeric characters, validate it)
//  * - adress: Option[String] (may be empty)
//  * - email: String (required)
//  * Response:
//  * - userId: Long
//  *
//  * Task 2b: [GET] Route for retrieving a User by ID
//  * GET /user?id=
//  * Query parameter:
//  * - id: Long
//  * Response:
//  * - user: User (as a JSON object)
//  * Example response:
//  * {
//  *   "id": 1,
//  *   "username": "Luke Skywalker",
//  *   "email": "lukesky@example.com"
//  *  }
//  *
//  * Akka HTTP docs: https://doc.akka.io/docs/akka-http/current/index.html?language=scala
//**/
//
///**
//  * Requirements:
//  * 1. Use async retry method from Task 3 when inserting to DB (result should be Either[String, UserId]).
//  * 2. Re-use your Validator instances from Task 3 for validating username.
//  * 3. Try to re-use parts of async User repository from Task 3.
//**/
//
///**
//  * Bonus task: Create an implicit exception handler for your routes.
//  * It should handle DbException or similar,and return response with both:
//  *  - StatusCodes.InternalServerError (500)
//  *  - payload with the description of the error.
//  * Use standard Exception types or inherit your own from Exception if you need.
//  *
// **/
//
//import scala.io.StdIn
//
//case class User(id: Long, username: String, address: Option[String], email: String)
//
//object UserRoutes {
//  val routes =
//    pathPrefix("user"){
//      ??? //IMPLEMENT
//    }
//}
//
//object App {
//  def main(args: Array[String]): Unit = {
//    implicit val system = ActorSystem("my-system")
//    implicit val materializer = ActorMaterializer()
//    implicit val executionContext = system.dispatcher
//
//    val bindingFuture = Http().bindAndHandle(UserRoutes.routes, "localhost", 8080)
//
//    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
//    StdIn.readLine() // let it run until user presses return
//    bindingFuture
//      .flatMap(_.unbind()) // trigger unbinding from the port
//      .onComplete(_ => system.terminate()) // and shutdown when done
//  }
//}
//
