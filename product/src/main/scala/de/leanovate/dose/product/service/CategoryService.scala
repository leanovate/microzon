package de.leanovate.dose.product.service

import akka.actor.ActorRefFactory
import spray.routing.HttpService
import spray.httpx.SprayJsonSupport._
import de.leanovate.dose.product.Akka
import de.leanovate.dose.product.model.{Category, Categories}
import spray.http.StatusCodes
import de.leanovate.dose.product.repository.CategoryRepository

class CategoryService(val actorRefFactory: ActorRefFactory) extends HttpService {

  import Akka._

  val routes = {
    pathPrefix("categories") {
      pathEnd {
        get {
          onSuccess(CategoryRepository.findAllActive().map(Categories.apply)) {
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
        path(Segment) {
          id =>
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
              } ~ delete {
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
