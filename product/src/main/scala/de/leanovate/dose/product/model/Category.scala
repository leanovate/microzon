package de.leanovate.dose.product.model

import reactivemongo.bson.{BSONObjectID, Macros}
import spray.json.DefaultJsonProtocol
import de.leanovate.dose.product.util.TypeMapper._

case class Category(
                     _id: Option[BSONObjectID],
                     name: String,
                     parent_id: Option[BSONObjectID]
                     )

object Category extends DefaultJsonProtocol {
  implicit val categoryFormat = jsonFormat(Category.apply, "id", "name", "parent_id")

  implicit val categoryHandler = Macros.handler[Category]
}
