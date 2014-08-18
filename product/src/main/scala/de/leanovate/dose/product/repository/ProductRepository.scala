package de.leanovate.dose.product.repository

import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.BSONDocument
import de.leanovate.dose.product.model.ActiveProduct
import de.leanovate.dose.product.Akka

object ProductRepository {

  import Akka._

  val products = Mongo.productsDb.collection[BSONCollection]("products")

  def findAll() = {
    products.find(BSONDocument()).cursor[ActiveProduct].collect[Seq]()
  }
}
