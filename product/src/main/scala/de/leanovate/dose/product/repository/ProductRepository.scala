package de.leanovate.dose.product.repository

import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.{BSONObjectID, BSONDocument}
import de.leanovate.dose.product.model.ActiveProduct
import de.leanovate.dose.product.Akka
import akka.event.slf4j.SLF4JLogging

object ProductRepository extends SLF4JLogging {

  import Akka._

  val products = Mongo.productsDb.map(_.collection[BSONCollection]("products"))

  def findAllActive() = {
    log.info("Get all products")
    products.flatMap(_.find(BSONDocument()).sort(BSONDocument("name" -> 1)).cursor[ActiveProduct].collect[Seq]())
  }

  def findAllForCategory(categoryId: String) = {
    log.info(s"Find all products of category $categoryId")
    products.flatMap(_.find(BSONDocument("categories" -> BSONObjectID(categoryId))).sort(BSONDocument("name" -> 1)).cursor[ActiveProduct].collect[Seq]())
  }

  def findById(id: String) = {
    log.info(s"Get product $id")
    products.flatMap(_.find(BSONDocument("_id" -> BSONObjectID(id))).cursor[ActiveProduct].headOption)
  }

  def insert(product: ActiveProduct) = {
    val toInsert = product.copy(_id = Some(BSONObjectID.generate))
    products.flatMap(_.insert(toInsert).map(_ => toInsert))
  }

  def update(id: String, product: ActiveProduct) = {
    val toUpdate = product.copy(_id = Some(BSONObjectID(id)))
    products.flatMap(_.update(BSONDocument("_id" -> BSONObjectID(id)), toUpdate).map(_ => toUpdate))
  }

  def deleteById(id: String) = {
    products.flatMap(_.remove(BSONDocument("_id" -> BSONObjectID(id))))
  }
}
