import BuildSettings._
import Dependencies._


lazy val testDeps = Seq(scalatest, scalacheck)

lazy val commonDeps = testDeps
lazy val catsDeps = Seq(cats)
lazy val akka = Seq(akkaStream)

lazy val photodb = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= commonDeps ++ catsDeps
  ).aggregate(backend, frontend)


lazy val frontend = (project in file("frontend"))
  .enablePlugins(PlayScala)
  .settings(commonSettings: _*)
  .settings(
    Seq(
      libraryDependencies ++= commonDeps ++ Seq(guice)
    )
  )
  .dependsOn(backend)

lazy val backend = (project in file("backend"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= commonDeps ++ catsDeps ++ akka
  )


