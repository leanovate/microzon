package models.consul

import play.api.libs.json.Json

case class HealthInfo(
                       Node: NodeInfo,
                       Service: ServiceInfo,
                       Checks: Seq[CheckInfo]
                       )

object HealthInfo {
  implicit val jsonFormat = Json.format[HealthInfo]

}
