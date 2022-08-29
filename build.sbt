import Dependencies._

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "MarsRover",
    libraryDependencies ++= Seq(
      cats,
      catsEffect,
      scalactic,
      scalaTest,
      catsScalaTest
    )
  )
