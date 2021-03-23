ThisBuild / scalaVersion := "2.13.5"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.baris.zquery101"
ThisBuild / organizationName := "example"

val zioVersion = "1.0.5"
val tranzactio = "2.0.0"
lazy val root = (project in file("."))
  .settings(
    name := "ZQuery101",
    scalacOptions += "-Yrangepos",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % zioVersion,
      "dev.zio" %% "zio-test" % zioVersion % Test,
      "dev.zio" %% "zio-test-magnolia" % zioVersion % Test,
      "io.github.gaelrenoux" %% "tranzactio" % tranzactio
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )

addCompilerPlugin(
  "org.scalameta" % "semanticdb-scalac" % "4.4.10" cross CrossVersion.full
)
