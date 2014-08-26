package models.product

import play.api.libs.json.Json

case class ImageRef(thumbnail: String,
                    preview: String,
                    original: String)

object ImageRef {
  implicit val jsonFormat = Json.format[ImageRef]
}