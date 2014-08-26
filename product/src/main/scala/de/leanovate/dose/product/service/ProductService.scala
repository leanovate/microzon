package de.leanovate.dose.product.service

import akka.actor.ActorRefFactory
import spray.routing._
import spray.httpx.SprayJsonSupport._
import de.leanovate.dose.product.model.{Products, ActiveProduct}
import de.leanovate.dose.product.repository.ProductRepository
import de.leanovate.dose.product.Akka
import spray.http.StatusCodes
import de.leanovate.dose.product.logging.CorrelatedRouting._
import akka.event.slf4j.SLF4JLogging

class ProductService(val actorRefFactory: ActorRefFactory) extends HttpService with SLF4JLogging {

  import Akka._

  val routes = {
    pathPrefix("products") {
      pathEnd {
        get {
          log.info("Query all product")
          onSuccessWithMdc(ProductRepository.findAllActive().map(Products.apply)) {
            products =>
              log.info(s"Found ${products.activeProducts.size} active products")
              complete(products)
          }
        } ~
          post {
            decompressRequest() {
              entity(as[ActiveProduct]) {
                product =>
                  onSuccessWithMdc(ProductRepository.insert(product)) {
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
              log.info(s"Looking up product ${id}")
              onSuccessWithMdc(ProductRepository.findById(id)) {
                case Some(product) =>
                  log.info(s"Found product ${id}")
                  complete(product)
                case None =>
                  log.info(s"Product ${id} not found")
                  respondWithStatus(StatusCodes.NotFound) {
                    complete("")
                  }
              }
            } ~
              put {
                decompressRequest() {
                  entity(as[ActiveProduct]) {
                    product =>
                      onSuccessWithMdc(ProductRepository.update(id, product)) {
                        updated =>
                          complete(updated)
                      }
                  }
                }
              } ~ delete {
              onSuccessWithMdc(ProductRepository.deleteById(id)) {
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