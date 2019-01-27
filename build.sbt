import BuildSettings._
import Dependencies._


lazy val testDeps = Seq(scalatest, scalacheck)

lazy val commonDeps = testDeps

lazy val photodb = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= commonDeps
  )



