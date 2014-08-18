package de.leanovate.dose.product.model

import spray.json.DefaultJsonProtocol

import ProductOptionProtocol._

case class ActiveProduct(
                          id: Option[String],
                          name: String,
                          description: Option[String],
                          options: Seq[ProductOption]
                          )

object ActiveProductProtocol extends DefaultJsonProtocol {
  implicit val activeProductFormat = jsonFormat4(ActiveProduct)
}

