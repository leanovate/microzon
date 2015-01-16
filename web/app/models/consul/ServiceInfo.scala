package models.consul

import play.api.libs.json.Json

case class ServiceInfo(
                        ID: String,
                        Service: String,
                        Tags: Seq[String],
                        Port: Int
                        )

object ServiceInfo {
  implicit val jsonFormat = Json.format[ServiceInfo]

}