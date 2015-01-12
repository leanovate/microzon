package de.leanovate.dose.cart.consul

case class ServiceNode(
                        Node: String,
                        Address: String,
                        ServiceID: String,
                        ServiceName: String,
                        ServiceTags: Seq[String],
                        ServicePort: Int
                        )