package backend

import java.net.URLEncoder

import logging.{CorrelatedLogging, CorrelatedWS, CorrelationContext}
import models.cart.{Cart, CartItem, CartItems}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import scaldi.{Injectable, Injector}

class CartBackend(implicit inj: Injector) extends Injectable with CorrelatedLogging {
  private val correlatedWS = inject[CorrelatedWS]
  private val serviceFailover = inject[ServiceFailover]

  import backend.CartBackend._

  def getCart(cartId: String)(implicit collectionContext: CorrelationContext) =
    serviceFailover.retry(serviceName, "/carts/" + URLEncoder.encode(cartId, "UTF-8")) {
      url =>
        correlatedWS.get(serviceName, url).map {
          response =>
            if (response.status != 200)
              throw new RuntimeException(s"get cart $cartId via cart service failed status=${response.status}")
            response.json.as[Cart]
        }
    }

  def createCart()(implicit collectionContext: CorrelationContext) =
    serviceFailover.retry(serviceName, "/carts") {
      url =>
        correlatedWS.post(serviceName, url, "{}").map {
          response =>
            if (response.status != 200)
              throw new RuntimeException(s"create cart via cart service failed status=${response.status}")
            response.json.as[Cart]
        }
    }

  def addToCart(cartItem: CartItem)(implicit collectionContext: CorrelationContext) =
    serviceFailover.retry(serviceName, "/carts/" + URLEncoder.encode(cartItem.cartId, "UTF-8") + "/items") {
      url =>
        correlatedWS.post(serviceName, url, Json.toJson(cartItem)).map {
          response =>
            if (response.status != 200)
              throw new RuntimeException(s"create cart via cart service failed status=${response.status}")
            response.json.as[CartItem]
        }
    }

  def getCartItems(cartId: String)(implicit collectionContext: CorrelationContext) =
    serviceFailover.retry(serviceName, "/carts/" + URLEncoder.encode(cartId, "UTF-8") + "/items") {
      url =>
        correlatedWS.get(serviceName, url).map {
          response =>
            if (response.status != 200)
              throw new RuntimeException(s"create cart via cart service failed status=${response.status}")
            response.json.as[CartItems]
        }
    }
}

object CartBackend {
  val serviceName = "cart-service"
}