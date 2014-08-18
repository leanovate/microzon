package de.leanovate.dose.product

import akka.actor.{Props, ActorSystem}
import de.leanovate.dose.product.service.HttpServiceActor
import akka.io.IO
import spray.can.Http

object Application extends App {
  implicit val system = ActorSystem("spray-can-do")

  val httpService = system.actorOf(Props[HttpServiceActor], "http-service")

  IO(Http) ! Http.Bind(httpService, "localhost", port = 8080)
}
