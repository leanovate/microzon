package de.leanovate.dose.product.service

import akka.actor.Actor
import spray.routing.HttpService
import de.leanovate.dose.product.logging.CorrelatedRouting._

class HttpServiceActor extends Actor with HttpService {
  val productService = new ProductService(context)
  val categoryService = new CategoryService(context)

  def actorRefFactory = context

  def receive = runRoute(routes)

  val routes = {
    correlationContext {
      implicit correlation =>
        path("ping") {
          get {
            complete("PONG!")
          }
        } ~
          productService.routes ~
          categoryService.routes
    }
  }
}
