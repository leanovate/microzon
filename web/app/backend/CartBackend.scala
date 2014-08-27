package backend

import java.net.URLEncoder

import models.cart.{CartItems, CartItem, Cart}
import play.api.libs.json.Json
import scaldi.{Injectable, Injector}
import logging.{CorrelatedWS, CorrelationContext, CorrelatedLogging}
import play.api.libs.concurrent.Execution.Implicits._

class CartBackend(implicit inj: Injector) extends Injectable with CorrelatedLogging {
  private val correlatedWS = inject[CorrelatedWS]
  private val baseUrl = inject[String](identified by "service.cart.url")
  private val name = "Cart"

  def getCart(cartId: String)(implicit collectionContext: CorrelationContext) = {
    correlatedWS.get(name, baseUrl + "/carts/" + URLEncoder.encode(cartId, "UTF-8")).map {
      response =>
        if (response.status != 200)
          throw new RuntimeException(s"get cart $cartId via cart service failed status=${response.status}")
        response.json.as[Cart]
    }
  }

  def createCart()(implicit collectionContext: CorrelationContext) = {
    correlatedWS.post(name, baseUrl + "/carts", "{}").map {
      response =>
        if (response.status != 200)
          throw new RuntimeException(s"create cart via cart service failed status=${response.status}")
        response.json.as[Cart]
    }
  }

  def addToCart(cartItem: CartItem)(implicit collectionContext: CorrelationContext) = {
    correlatedWS.post(name, baseUrl + "/carts/" + URLEncoder.encode(cartItem.cartId, "UTF-8") + "/items", Json.toJson(cartItem)).map {
      response =>
        if (response.status != 200)
          throw new RuntimeException(s"create cart via cart service failed status=${response.status}")
        response.json.as[CartItem]
    }
  }

  def getCartItems(cartId: String)(implicit collectionContext: CorrelationContext) = {
    correlatedWS.get(name, baseUrl + "/carts/" + URLEncoder.encode(cartId, "UTF-8") + "/items").map {
      response =>
        if (response.status != 200)
          throw new RuntimeException(s"create cart via cart service failed status=${response.status}")
        response.json.as[CartItems]
    }
  }
}
