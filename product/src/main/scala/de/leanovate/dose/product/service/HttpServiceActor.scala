package de.leanovate.dose.product.service

import akka.actor.Actor
import spray.routing.HttpService

class HttpServiceActor extends Actor with HttpService {
  val productService = new ProductService(context)

  def actorRefFactory = context

  def receive = runRoute(routes)

  val routes = {
    path("ping") {
      get {
        complete("PONG!")
      }
    } ~
      productService.routes
  }
}
