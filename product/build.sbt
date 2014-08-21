import AssemblyKeys._

name := """product"""

version := "0.1.0"

scalaVersion := "2.10.4"

resolvers += "spray" at "http://repo.spray.io/"

resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= {
  val akkaV = "2.3.5"
  val sprayV = "1.3.1"
  Seq(
    "io.spray" % "spray-can" % sprayV,
    "io.spray" % "spray-routing" % sprayV,
    "io.spray" %% "spray-json" % "1.2.6",
    "org.reactivemongo" %% "reactivemongo" % "0.10.5.akka23-SNAPSHOT",
    "ch.qos.logback" % "logback-classic" % "1.1.2",
    "io.netty" % "netty" % "3.6.5.Final" excludeAll(
      ExclusionRule(organization = "org.apache.logging.log4j")
      ),
    "io.spray" % "spray-testkit" % sprayV % "test",
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-slf4j" % akkaV,
    "com.typesafe.akka" %% "akka-testkit" % akkaV % "test",
    "org.specs2" %% "specs2-core" % "2.3.7" % "test"
  )
}

assemblySettings

net.virtualvoid.sbt.graph.Plugin.graphSettings

mainClass in assembly := Some("de.leanovate.dose.product.Application")

target in assembly := baseDirectory.value / ".." / "vagrant" / "dists"
