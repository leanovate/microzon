package de.leanovate.dose.product.repository

import de.leanovate.dose.product.Akka
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.BSONDocument
import de.leanovate.dose.product.model.Category
import tyrex.services.UUID

object CategoryRepository {

  import Akka._

  val categories = Mongo.productsDb.collection[BSONCollection]("categories")

  def findAllActive() = {
    categories.find(BSONDocument()).cursor[Category].collect[Seq]()
  }

  def findById(id: String) = {
    categories.find(BSONDocument("id" -> id)).cursor[Category].headOption
  }

  def insert(category: Category) = {
    val toInsert = category.copy(id = Some(UUID.create()))
    categories.insert(toInsert).map(_ => toInsert)
  }

  def update(id: String, category: Category) = {
    val toUpdate = category.copy(id = Some(id))
    categories.update(BSONDocument("id" -> id), toUpdate).map(_ => toUpdate)
  }

  def deleteById(id:String) = {
    categories.remove(BSONDocument("id" -> id))
  }
}
