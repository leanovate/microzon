package models.user

import play.api.libs.json.Json

case class Login(
                  email: String,
                  password: String
                  )

object Login {
  implicit val jsonWriters = Json.writes[Login]
}