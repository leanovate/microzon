package models.billing

import play.api.libs.json.Json

case class CreateOrder(customerId: Long,
                       cartId: String)

object CreateOrder {
  implicit val jsonFormat = Json.format[CreateOrder]
}