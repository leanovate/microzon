package de.leanovate.dose.product.repository

import de.leanovate.dose.product.Akka
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.{BSONObjectID, BSONDocument}
import de.leanovate.dose.product.model.Category
import akka.event.slf4j.SLF4JLogging

object CategoryRepository extends SLF4JLogging {

  import Akka._

  val categories = Mongo.productsDb.collection[BSONCollection]("categories")

  def findAll() = {
    log.info("Get all categories")
    categories.find(BSONDocument()).sort(BSONDocument("name" -> 1)).cursor[Category].collect[Seq]()
  }

  def findById(id: String) = {
    categories.find(BSONDocument("_id" -> BSONObjectID(id))).cursor[Category].headOption
  }

  def insert(category: Category) = {
    val toInsert = category.copy(_id = Some(BSONObjectID.generate))
    categories.insert(toInsert).map(_ => toInsert)
  }

  def update(id: String, category: Category) = {
    val toUpdate = category.copy(_id = Some(BSONObjectID(id)))
    categories.update(BSONDocument("_id" -> BSONObjectID(id)), toUpdate).map(_ => toUpdate)
  }

  def deleteById(id: String) = {
    categories.remove(BSONDocument("_id" -> BSONObjectID(id)))
  }
}
