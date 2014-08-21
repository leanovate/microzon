name := """cart"""

version := "0.1.0"

scalaVersion := "2.11.1"


libraryDependencies ++= Seq(
    "org.scalatra" %% "scalatra" % "2.3.0",
    "org.scalikejdbc" %% "scalikejdbc" % "2.0.7",
    "org.flywaydb" % "flyway-core" % "3.0",
    "mysql" % "mysql-connector-java" % "5.1.32",
    "ch.qos.logback" % "logback-classic" % "1.1.2",
    "org.eclipse.jetty" % "jetty-webapp" % "8.1.8.v20121106"
)