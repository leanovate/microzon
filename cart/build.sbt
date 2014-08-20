name := """cart"""

version := "0.1.0"

scalaVersion := "2.11.1"


libraryDependencies ++= Seq(
    "org.scalatra" %% "scalatra" % "2.3.0",
    "org.scalikejdbc" %% "scalikejdbc" % "2.0.7",
    "ch.qos.logback" % "logback-classic" % "1.1.2",
    "org.eclipse.jetty" % "jetty-webapp" % "8.1.8.v20121106"
)