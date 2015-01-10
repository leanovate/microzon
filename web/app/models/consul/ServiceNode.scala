package models.consul

import play.api.libs.json.Json

case class ServiceNode(
  Node: String,
  Address: String,
  ServiceID: String,
  ServiceName: String,
  ServiceTags: Seq[String],
  ServicePort: Int
  )

object ServiceNode {
  implicit val jsonFormat = Json.format[ServiceNode]
}