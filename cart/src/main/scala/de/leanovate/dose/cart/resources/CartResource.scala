package de.leanovate.dose.cart.resources

import com.twitter.finatra.Controller
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import de.leanovate.dose.cart.model.{CartItem, Cart}
import de.leanovate.dose.cart.repository.{CartItemRepository, CartRepository}
import com.twitter.finagle.tracing.Trace
import de.leanovate.dose.cart.util.Json
import de.leanovate.dose.cart.connectors.ProductConnector
import com.twitter.util.Future

class CartResource extends Controller {
  post("/carts") {
    request =>
      val cart = Json.readValue(request.contentString, classOf[Cart])

      CartRepository.insert(cart).map(render.json)
  }

  get("/carts/:id") {
    request =>
      CartRepository.findById(request.routeParams("id")).map {
        case Some(cart) =>
          render.json(cart)
        case None =>
          render.notFound
      }
  }

  get("/carts/:id/items") {
    request =>
      CartItemRepository.findAllForCart(request.routeParams("id")).flatMap {
        items =>
          val withProducts = items.map {
            item =>
              ProductConnector.getProduct(item.productId).map(product => item.copy(product = product))
          }
          Future.collect(withProducts).map(render.json)
      }
  }

  post("/carts/:id/items") {
    request =>
      val cartItem = Json.readValue(request.contentString, classOf[CartItem])
      CartRepository.findById(request.routeParams("id")).flatMap {
        case Some(cart) =>
          CartItemRepository.insertToCart(cart, cartItem).map(render.json)
        case None =>
          render.notFound.toFuture
      }
  }
}
