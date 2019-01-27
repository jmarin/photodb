import BuildSettings._
import Dependencies._


lazy val testDeps = Seq(scalatest, scalacheck)

lazy val commonDeps = testDeps
lazy val catsDeps = Seq(cats)

lazy val photodb = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= commonDeps ++ catsDeps
  ).aggregate(backend, frontend)


lazy val frontend = (project in file("frontend"))
  .settings(commonSettings: _*)
  .dependsOn(backend)

lazy val backend = (project in file("backend"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= commonDeps ++ catsDeps
  )


