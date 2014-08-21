package models.product

import play.api.libs.json.Json

case class ActiveProduct(
                          id: String,
                          name: String,
                          description: Option[String],
                          options: Seq[ProductOption],
                          categories: Seq[String],
                          images: Seq[ImageRef]
                          )

object ActiveProduct {
  implicit val jsonReads = Json.reads[ActiveProduct]
}
