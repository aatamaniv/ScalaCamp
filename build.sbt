import Dependencies._

ThisBuild / scalaVersion := "2.12.8"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.atamaniv"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "ScalaCamp",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      "org.typelevel" %% "cats-core" % "1.5.0-RC1",
      "com.typesafe.akka" %% "akka-actor" % "2.5.22",
      "com.typesafe.akka" %% "akka-testkit" % "2.5.22" % Test,
      "com.typesafe.akka" %% "akka-stream" % "2.5.22",
      "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.22" % Test,
      "com.typesafe.akka" %% "akka-http" % "10.1.8",
      "com.typesafe.akka" %% "akka-http-testkit" % "10.1.8" % Test,
      "com.h2database" % "h2" % "1.3.166",
      "com.typesafe.slick" %% "slick" % "3.2.0",
      "org.slf4j" % "slf4j-nop" % "1.6.4",
      "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.8"
    )
  )