package de.leanovate.dose.product.model

import spray.json.DefaultJsonProtocol

case class Products(activeProducts:Seq[ActiveProduct])

object Products extends DefaultJsonProtocol {
  implicit val productsFormat = jsonFormat1(Products.apply)
}