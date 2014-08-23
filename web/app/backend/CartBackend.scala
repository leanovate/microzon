package backend

import java.net.URLEncoder

import models.cart.{CartItem, Cart}
import play.api.libs.json.Json
import scaldi.{Injectable, Injector}
import logging.{CorrelatedWS, CorrelationContext, CorrelatedLogging}
import play.api.libs.concurrent.Execution.Implicits._

class CartBackend(implicit inj: Injector) extends Injectable with CorrelatedLogging {
  private val baseUrl = inject[String](identified by "service.cart.url")

  def getCart(cartId:String)(implicit collectionContext: CorrelationContext) = {
    CorrelatedWS.url(baseUrl + "/carts/" + URLEncoder.encode(cartId, "UTF-8")).get().map {
      response =>
        if (response.status != 200)
          throw new RuntimeException(s"get cart $cartId via cart service failed status=${response.status}")
        response.json.as[Cart]
    }
  }

  def createCart()(implicit collectionContext: CorrelationContext) = {
    CorrelatedWS.url(baseUrl + "/carts").post("{}").map {
      response =>
        if (response.status != 200)
          throw new RuntimeException(s"create cart via cart service failed status=${response.status}")
        response.json.as[Cart]
    }
  }

  def addToCart(cartItem:CartItem)(implicit collectionContext: CorrelationContext) = {
    CorrelatedWS.url(baseUrl + "/carts/" + URLEncoder.encode(cartItem.cartId, "UTF-8") + "/items").post(Json.toJson(cartItem)).map {
      response =>
        if (response.status != 200)
          throw new RuntimeException(s"create cart via cart service failed status=${response.status}")
        response.json.as[CartItem]
    }
  }
}
