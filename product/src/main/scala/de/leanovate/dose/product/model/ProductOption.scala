package de.leanovate.dose.product.model

import spray.json.DefaultJsonProtocol

case class ProductOption(
                          name: String,
                          description: Option[String],
                          priceInCent: Int
                          )

object ProductOptionProtocol extends DefaultJsonProtocol {
  implicit val productOptionFormat = jsonFormat3(ProductOption)
}