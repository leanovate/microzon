package de.leanovate.dose.product.consul

import spray.json.{JsonFormat, DefaultJsonProtocol}

case class ServiceNode(
  Node: String,
  Address: String,
  ServiceID: String,
  ServiceName: String,
  ServiceTags: Seq[String],
  ServicePort: Int
  )

object ServiceNode extends DefaultJsonProtocol {
  implicit val serviceNodeFormat : JsonFormat[ServiceNode]= jsonFormat6(ServiceNode.apply)
}