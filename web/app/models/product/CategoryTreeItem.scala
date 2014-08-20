package models.product

import play.api.libs.json.Json

case class CategoryTreeItem(category:Category, children:Seq[CategoryTreeItem])

object CategoryTreeItem {
  implicit val jsonReads = Json.reads[CategoryTreeItem]
}