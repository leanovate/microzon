package models.consul

import play.api.libs.json.Json

case class NodeInfo(
                     Node: String,
                     Address: String
                     )

object NodeInfo {
  implicit val jsonFormat = Json.format[NodeInfo]
}