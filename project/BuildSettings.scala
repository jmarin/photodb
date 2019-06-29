import sbt.Keys._
import sbt._

object BuildSettings {
  val buildOrganization = "com.jmarin"
  val buildVersion = "1.0-SNAPSHOT"
  val buildScalaVersion = "2.13.0"

  val commonSettings = Defaults.coreDefaultSettings ++
    Seq(
      organization := buildOrganization,
      version := buildVersion,
      scalaVersion := buildScalaVersion,
      scalacOptions ++= Seq(
        "-Xlint",
        "-deprecation",
        "-unchecked",
        "-feature",
        "-language:higherKinds"
      ),
      parallelExecution in Test := true,
      fork in Test := true
    )

}


