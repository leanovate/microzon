package models.consul

import play.api.libs.json.Json

case class CheckInfo(
                      Node: String,
                      CheckID: String,
                      Name: String,
                      Status: String,
                      Notes: String,
                      Output: String,
                      ServiceID: String,
                      ServiceName: String
                      )

object CheckInfo {
  implicit val jsonFormat = Json.format[CheckInfo]
}