package de.leanovate.dose.product.model

import spray.json.DefaultJsonProtocol

import reactivemongo.bson.Macros

case class ActiveProduct(
                          id: Option[String],
                          name: String,
                          description: Option[String],
                          options: Seq[ProductOption]
                          )

object ActiveProduct extends DefaultJsonProtocol {
  implicit val activeProductFormat = jsonFormat4(ActiveProduct.apply)

  implicit val activeProductHandler = Macros.handler[ActiveProduct]

}
