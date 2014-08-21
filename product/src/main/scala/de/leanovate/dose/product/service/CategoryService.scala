package de.leanovate.dose.product.service

import akka.actor.ActorRefFactory
import spray.routing.HttpService
import spray.httpx.SprayJsonSupport._
import de.leanovate.dose.product.Akka
import de.leanovate.dose.product.model.{Products, Category, Categories}
import spray.http.StatusCodes
import de.leanovate.dose.product.repository.{ProductRepository, CategoryRepository}
import de.leanovate.dose.product.logging.CorrelatedRouting._
import de.leanovate.dose.product.logging.CorrelationContext

class CategoryService(val actorRefFactory: ActorRefFactory) extends HttpService {

  import Akka._

  def routes(implicit correlationContext: CorrelationContext) = {
    pathPrefix("categories") {
      pathEnd {
        get {
          onSuccess(CategoryRepository.findAll().map(Categories.apply)) {
            categories =>
              complete(categories)
          }
        } ~
          post {
            decompressRequest() {
              entity(as[Category]) {
                category =>
                  onSuccess(CategoryRepository.insert(category)) {
                    inserted =>
                      complete(inserted)
                  }
              }
            }
          }
      } ~
        pathPrefix(Segment) {
          id =>
            path("products") {
              get {
                onSuccess(ProductRepository.findAllForCategory(id).map(Products.apply)) {
                  products =>
                    complete(products)
                }
              }
            } ~
              get {
                onSuccess(CategoryRepository.findById(id)) {
                  case Some(category) =>
                    complete(category)
                  case None =>
                    respondWithStatus(StatusCodes.NotFound) {
                      complete("")
                    }
                }
              } ~
              put {
                decompressRequest() {
                  entity(as[Category]) {
                    category =>
                      onSuccess(CategoryRepository.update(id, category)) {
                        updated =>
                          complete(updated)
                      }
                  }
                }
              } ~
              delete {
                onSuccess(CategoryRepository.deleteById(id)) {
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
