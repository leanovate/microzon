package de.leanovate.dose.product.repository

import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.BSONDocument
import de.leanovate.dose.product.model.ActiveProduct
import de.leanovate.dose.product.Akka
import tyrex.services.UUID
import de.leanovate.dose.product.logging.{CorrelatedLogging, CorrelationContext}

object ProductRepository extends CorrelatedLogging {

  import Akka._

  val products = Mongo.productsDb.collection[BSONCollection]("products")

  def findAllActive()(implicit correlationContext: CorrelationContext) = withMdc {
    log.info("Get all categories")
    products.find(BSONDocument()).sort(BSONDocument("name" -> 1)).cursor[ActiveProduct].collect[Seq]()
  }

  def findById(id: String)(implicit correlationContext: CorrelationContext) = withMdc {
    log.info(s"Get product $id")
    products.find(BSONDocument("id" -> id)).cursor[ActiveProduct].headOption
  }

  def insert(product: ActiveProduct)(implicit correlationContext: CorrelationContext) = withMdc {
    val toInsert = product.copy(id = Some(UUID.create()))
    products.insert(toInsert).map(_ => toInsert)
  }

  def update(id: String, product: ActiveProduct)(implicit correlationContext: CorrelationContext) = withMdc {
    val toUpdate = product.copy(id = Some(id))
    products.update(BSONDocument("id" -> id), toUpdate).map(_ => toUpdate)
  }

  def deleteById(id: String)(implicit correlationContext: CorrelationContext) = withMdc {
    products.remove(BSONDocument("id" -> id))
  }
}
