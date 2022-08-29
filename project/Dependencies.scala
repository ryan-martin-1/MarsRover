import sbt._

object Dependencies {
  object Versions {
    lazy val cats = "2.8.0"
    lazy val catsEffect = "3.3.14"
    lazy val catsScalaTest = "1.2.0"
    lazy val scalactic = "3.2.13"
    lazy val scalaTest = "3.2.13"

  }

  lazy val cats = "org.typelevel" %% "cats-core" % Versions.cats
  lazy val catsEffect = "org.typelevel" %% "cats-effect" % Versions.catsEffect
  lazy val catsScalaTest = "org.typelevel" %% "cats-effect-testing-scalatest" % Versions.catsScalaTest % Test
  lazy val scalactic = "org.scalactic" %% "scalactic" % Versions.scalactic
  lazy val scalaTest = "org.scalatest" %% "scalatest" % Versions.scalaTest % "test"
}
