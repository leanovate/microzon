package models.cart

import play.api.libs.json.Json


case class CartItems(
                      items: Seq[CartItem],
                      valid: Boolean,
                      totalCents: Int
                      )

object CartItems {
  implicit val jsonFormat = Json.format[CartItems]
}