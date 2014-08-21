package models.product

import play.api.libs.json.Json

case class Products(activeProducts: Seq[ActiveProduct])

object Products {
  implicit val jsonReads = Json.reads[Products]
}
