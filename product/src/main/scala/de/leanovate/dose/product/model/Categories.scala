package de.leanovate.dose.product.model

import spray.json._
import spray.json

case class CategoryTreeItem(category: Category, children: Seq[CategoryTreeItem])

case class Categories(categories: Seq[Category], tree: Seq[CategoryTreeItem])

object Categories extends DefaultJsonProtocol {
  implicit val categoryTreeItemFornat = new RootJsonWriter[CategoryTreeItem] {
    override def write(obj: CategoryTreeItem) = JsObject(
      "category" -> json.jsonWriter[Category].write(obj.category),
      "children" -> JsArray(obj.children.map(write).toList)
    )
  }

  implicit val categoriesFormat = new RootJsonWriter[Categories] {
    override def write(obj: Categories) = JsObject(
      "categories" -> json.jsonWriter[Seq[Category]].write(obj.categories),
      "tree" -> JsArray(obj.tree.map(categoryTreeItemFornat.write).toList)
    )
  }

  def apply(categories: Seq[Category]): Categories = {
    val byParentId = categories.groupBy(_.parent_id)

    def createTreeItem(category: Category): CategoryTreeItem = {
      CategoryTreeItem(category, byParentId.getOrElse(category.id, List()).map(createTreeItem))
    }

    Categories(categories, categories.filter(_.parent_id.isEmpty).map(createTreeItem))
  }
}