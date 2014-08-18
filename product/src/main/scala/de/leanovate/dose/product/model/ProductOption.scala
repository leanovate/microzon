package de.leanovate.dose.product.model

import spray.json.DefaultJsonProtocol
import reactivemongo.bson.{Macros, BSONDocumentReader, BSONDocument, BSONDocumentWriter}

case class ProductOption(
                          name: String,
                          description: Option[String],
                          priceInCent: Int
                          )

object ProductOption extends DefaultJsonProtocol {
  implicit val productOptionFormat = jsonFormat3(ProductOption.apply)

  implicit val productOptionHandler = Macros.handler[ProductOption]
}