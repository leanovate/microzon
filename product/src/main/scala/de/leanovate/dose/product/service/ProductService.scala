package de.leanovate.dose.product.service

import akka.actor.ActorRefFactory
import spray.routing._
import spray.httpx.SprayJsonSupport._
import de.leanovate.dose.product.model.{Products, ActiveProduct}
import de.leanovate.dose.product.repository.ProductRepository
import de.leanovate.dose.product.Akka

class ProductService(val actorRefFactory: ActorRefFactory) extends HttpService {

  import Akka._

  val routes = {
    pathPrefix("products") {
      pathEnd {
        get {
          onSuccess(ProductRepository.findAll().map(Products.apply)) {
            products =>
              complete(products)
          }
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
