package de.leanovate.dose.product.service

import akka.actor.ActorRefFactory
import spray.routing._
import spray.httpx.SprayJsonSupport._
import de.leanovate.dose.product.model.{Products, ActiveProduct}
import de.leanovate.dose.product.repository.ProductRepository
import de.leanovate.dose.product.Akka
import spray.http.StatusCodes

class ProductService(val actorRefFactory: ActorRefFactory) extends HttpService {

  import Akka._

  val routes = {
    pathPrefix("products") {
      pathEnd {
        get {
          onSuccess(ProductRepository.findAllActive().map(Products.apply)) {
            products =>
              complete(products)
          }
        } ~
          post {
            decompressRequest() {
              entity(as[ActiveProduct]) {
                product =>
                  onSuccess(ProductRepository.insert(product)) {
                    inserted =>
                      complete(inserted)
                  }
              }
            }
          }
      } ~
        path(Segment) {
          id =>
            get {
              onSuccess(ProductRepository.findById(id)) {
                case Some(product) =>
                  complete(product)
                case None =>
                  respondWithStatus(StatusCodes.NotFound) {
                    complete("")
                  }
              }
            } ~
              put {
                decompressRequest() {
                  entity(as[ActiveProduct]) {
                    product =>
                      onSuccess(ProductRepository.update(id, product)) {
                        updated =>
                          complete(updated)
                      }
                  }
                }
              } ~ delete {
              onSuccess(ProductRepository.deleteById(id)) {
                _ =>
                  respondWithStatus(StatusCodes.NoContent) {
                    complete("")
                  }
              }
            }
        }
    }
  }
}