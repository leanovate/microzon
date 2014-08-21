package de.leanovate.dose.product.model

import spray.json.DefaultJsonProtocol

import reactivemongo.bson.{BSONObjectID, Macros}
import de.leanovate.dose.product.util.TypeMapper._

case class ActiveProduct(
                          _id: Option[BSONObjectID],
                          name: String,
                          description: Option[String],
                          options: Seq[ProductOption],
                          categories: Seq[BSONObjectID],
                          images: Seq[ImageRef]
                          )

object ActiveProduct extends DefaultJsonProtocol {
  implicit val activeProductFormat = jsonFormat(ActiveProduct.apply, "id", "name", "description", "options", "categories", "images")

  implicit val activeProductHandler = Macros.handler[ActiveProduct]
}
