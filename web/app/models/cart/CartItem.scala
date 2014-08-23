package models.cart

import play.api.libs.json.Json

case class CartItem(cartId: String, position: Option[Long], productId: String, productOption: String, amount:Int)

object CartItem {
  implicit val jsonFormat = Json.format[CartItem]
}