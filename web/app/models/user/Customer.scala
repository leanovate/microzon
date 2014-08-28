package models.user

import play.api.libs.json.Json

case class Customer(
                     customerId: Long,
                     email: String,
                     password: Option[String],
                     firstName: Option[String],
                     lastName: Option[String]
                     )

object Customer {
  implicit val jsonFormat = Json.format[Customer]
}