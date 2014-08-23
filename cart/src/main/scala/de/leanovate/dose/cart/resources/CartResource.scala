package de.leanovate.dose.cart.resources

import com.twitter.finatra.Controller
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import de.leanovate.dose.cart.model.{CartItem, Cart}
import de.leanovate.dose.cart.repository.{CartItemRepository, CartRepository}
import com.twitter.finagle.tracing.Trace

class CartResource extends Controller {
  private lazy val jsonMapper = {
    val m = new ObjectMapper()
    m.registerModule(DefaultScalaModule)
  }

  post("/carts") {
    request =>
      val cart = jsonMapper.readValue(request.contentString, classOf[Cart])

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
      CartItemRepository.findAllForCart(request.routeParams("id")).map(render.json)
  }

  post("/carts/:id/items") {
    request =>
      val cartItem = jsonMapper.readValue(request.contentString, classOf[CartItem])
      CartRepository.findById(request.routeParams("id")).flatMap {
        case Some(cart) =>
          CartItemRepository.insertToCart(cart, cartItem).map(render.json)
        case None =>
          render.notFound.toFuture
      }
  }
}
