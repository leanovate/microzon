package models.product

import play.api.libs.json.Json

case class CategoryTree(tree: Seq[CategoryTreeItem])

object CategoryTree {
  implicit val jsonReads = Json.reads[CategoryTree]
}