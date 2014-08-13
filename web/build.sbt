name := """web"""

organization := "de.leanovate.dose"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "org.webjars" %% "webjars-play" % "2.3.0",
  "org.webjars" % "react" % "0.11.1",
  "org.webjars" % "es5-shim" % "2.3.0-2",
  "org.webjars" % "bootstrap" % "3.2.0"
)

