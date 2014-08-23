package models.cart

import play.api.libs.json.Json

case class Cart(id: String,
                itemCount: Int,
                lastPosition: Option[Int])

object Cart {
  implicit val jsonReads = Json.reads[Cart]
}