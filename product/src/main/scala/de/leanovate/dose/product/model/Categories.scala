package de.leanovate.dose.product.model

import spray.json.DefaultJsonProtocol

case class Categories(categories: Seq[Category])

object Categories extends DefaultJsonProtocol {
  implicit val categoriesFormat = jsonFormat1(Categories.apply)
}