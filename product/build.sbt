import AssemblyKeys._

name := """product"""

version := "0.1.0"

scalaVersion := "2.10.3"

resolvers += "spray" at "http://repo.spray.io/"

libraryDependencies ++= {
  val akkaV = "2.3.5"
  val sprayV = "1.3.1"
  Seq(
    "io.spray" % "spray-can" % sprayV,
    "io.spray" % "spray-routing" % sprayV,
    "io.spray" %% "spray-json" % "1.2.6",
    "tyrex" % "tyrex" % "1.0.1",
    "org.reactivemongo" %% "reactivemongo" % "0.10.0",
    "io.spray" % "spray-testkit" % sprayV % "test",
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-testkit" % akkaV % "test",
    "org.specs2" %% "specs2-core" % "2.3.7" % "test"
  )
}

assemblySettings
