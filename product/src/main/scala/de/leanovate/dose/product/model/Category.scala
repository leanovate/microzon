package de.leanovate.dose.product.model

import reactivemongo.bson.Macros
import spray.json.DefaultJsonProtocol

case class Category(
                     id: Option[String],
                     name: String,
                     parent_id: Option[String]
                     )

object Category extends DefaultJsonProtocol {
  implicit val categoryFormat = jsonFormat3(Category.apply)

  implicit val categoryHandler = Macros.handler[Category]
}
