package models.user

import play.api.libs.json.Json

case class RegistrationResult(successful:Boolean, customerId: Long)

object RegistrationResult {
  implicit val jsonReads = Json.reads[RegistrationResult]
}
