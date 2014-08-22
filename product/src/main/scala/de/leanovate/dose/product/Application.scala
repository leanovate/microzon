package de.leanovate.dose.product

import akka.actor.Props
import de.leanovate.dose.product.service.HttpServiceActor
import akka.io.IO
import spray.can.Http
import java.lang.management.ManagementFactory

object Application extends App {

  import Akka._

  val pid: String = ManagementFactory.getRuntimeMXBean.getName.split('@').head

  System.setProperty("PID", pid)

  val httpService = actorSystem.actorOf(Props[HttpServiceActor], "http-service")

  IO(Http) ! Http.Bind(httpService, "localhost", port = 8080)
}
