package de.leanovate.dose.product.service

import akka.actor.ActorRefFactory
import spray.routing._
import spray.httpx.SprayJsonSupport._
import de.leanovate.dose.product.model.ActiveProductProtocol._
import de.leanovate.dose.product.model.ActiveProduct

class ProductService(val actorRefFactory: ActorRefFactory) extends HttpService {

  val routes = {
    pathPrefix("products") {
      pathEnd {
        get {
          complete("The products are here")
        } ~
        post {
          decompressRequest() {
            entity(as[ActiveProduct]) {
              product =>
                complete("Got product " + product)
            }
          }
        }
      } ~
        path(Segment) {
          id =>
            get {
              complete(s"One product $id")
            }
        }

    }
  }
}
