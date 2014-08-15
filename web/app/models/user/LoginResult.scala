package models.user

import play.api.libs.json.Json

case class LoginResult(successful: Boolean, customerId: Long)

object LoginResult {
  implicit val jsonReads = Json.reads[LoginResult]
}
