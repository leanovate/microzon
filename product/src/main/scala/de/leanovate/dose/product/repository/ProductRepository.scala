package de.leanovate.dose.product.repository

import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.BSONDocument
import de.leanovate.dose.product.model.ActiveProduct
import de.leanovate.dose.product.Akka
import tyrex.services.UUID

object ProductRepository {

  import Akka._

  val products = Mongo.productsDb.collection[BSONCollection]("products")

  def findAllActive() = {
    products.find(BSONDocument()).cursor[ActiveProduct].collect[Seq]()
  }

  def findById(id: String) = {
    products.find(BSONDocument("id" -> id)).cursor[ActiveProduct].headOption
  }

  def insert(product: ActiveProduct) = {
    val toInsert = product.copy(id = Some(UUID.create()))
    products.insert(toInsert).map(_ => toInsert)
  }

  def update(id: String, product: ActiveProduct) = {
    val toUpdate = product.copy(id = Some(id))
    products.update(BSONDocument("id" -> id), toUpdate).map(_ => toUpdate)
  }

  def deleteById(id:String) = {
    products.remove(BSONDocument("id" -> id))
  }
}
