import sbt._
import Version._

object Dependencies {
  lazy val logback = "ch.qos.logback" % "logback-classic" % logbackVersion
  lazy val scalatest = "org.scalatest" %% "scalatest" % scalatestVersion % "test"
  lazy val scalacheck = "org.scalacheck" %% "scalacheck" % scalacheckVersion % "test"
  lazy val cats = "org.typelevel" %% "cats-core" % catsVersion
}



