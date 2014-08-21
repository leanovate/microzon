package de.leanovate.dose.product.util

import spray.json._
import reactivemongo.bson.BSONObjectID

object TypeMapper {
  implicit val objectIdFormat = new JsonFormat[BSONObjectID] {
    override def read(json: JsValue) = json match {
      case JsString(str) => BSONObjectID(str)
      case _ => throw new DeserializationException("ObjectId expected")
    }

    override def write(obj: BSONObjectID) = JsString(obj.stringify)
  }
}
