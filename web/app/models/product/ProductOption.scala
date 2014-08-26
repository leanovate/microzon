package models.product

import play.api.libs.json.Json

case class ProductOption(
                          name: String,
                          description: Option[String],
                          priceInCent: Int
                          )

object ProductOption {
  implicit val jsonFormat = Json.format[ProductOption]
}