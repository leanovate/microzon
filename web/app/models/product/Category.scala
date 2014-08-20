package models.product

import play.api.libs.json.Json

case class Category(id: String, name: String)

object Category {
  implicit val jsonReads = Json.reads[Category]
}