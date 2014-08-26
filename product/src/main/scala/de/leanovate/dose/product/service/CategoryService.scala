package de.leanovate.dose.product.service

import akka.actor.ActorRefFactory
import spray.routing.HttpService
import spray.httpx.SprayJsonSupport._
import de.leanovate.dose.product.Akka
import de.leanovate.dose.product.model.{Products, Category, Categories}
import de.leanovate.dose.product.repository.{ProductRepository, CategoryRepository}
import de.leanovate.dose.product.logging.CorrelatedRouting._

class CategoryService(val actorRefFactory: ActorRefFactory) extends HttpService {

  import Akka._

  val routes = {
    pathPrefix("categories") {
      pathEnd {
        get {
          onSuccessWithMdc(CategoryRepository.findAll().map(Categories.apply)) {
            categories =>
              complete(categories)
          }
        } ~
          post {
            decompressRequest() {
              entity(as[Category]) {
                category =>
                  onSuccessWithMdc(CategoryRepository.insert(category)) {
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
                onSuccessWithMdc(ProductRepository.findAllForCategory(id).map(Products.apply)) {
                  products =>
                    complete(products)
                }
              }
            }
        }
    }
  }
}
