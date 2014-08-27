package models.cart

import play.api.libs.json.Json
import models.product.ActiveProduct

case class CartItem(cartId: String,
                    position: Option[Long],
                    productId: String,
                    productOption: String,
                    amount: Int,
                    product: Option[ActiveProduct],
                    priceInCent: Option[Int])

object CartItem {
  implicit val jsonFormat = Json.format[CartItem]
}