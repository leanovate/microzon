package models.user

import play.api.libs.json.Json

case class Registration(
                         email: String,
                         password: String,
                         passwordRepeat: String
                         )

object Registration {
  implicit val jsonWrites = Json.writes[Registration]
}