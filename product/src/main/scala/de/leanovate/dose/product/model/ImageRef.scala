package de.leanovate.dose.product.model

import spray.json.DefaultJsonProtocol
import reactivemongo.bson.{BSONObjectID, Macros}
import de.leanovate.dose.product.util.TypeMapper._

case class ImageRef(thumbnail: BSONObjectID,
                    preview: BSONObjectID,
                    original: BSONObjectID)

object ImageRef extends DefaultJsonProtocol {
  implicit val activeProductFormat = jsonFormat3(ImageRef.apply)

  implicit val activeProductHandler = Macros.handler[ImageRef]
}
