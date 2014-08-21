package de.leanovate.dose.product.repository

import de.leanovate.dose.product.Akka
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.BSONDocument
import de.leanovate.dose.product.model.Category
import tyrex.services.UUID
import de.leanovate.dose.product.logging.{CorrelationContext, CorrelatedLogging}

object CategoryRepository extends CorrelatedLogging {

  import Akka._

  val categories = Mongo.productsDb.collection[BSONCollection]("categories")

  def findAll()(implicit correlationContext: CorrelationContext) = withMdc {
    log.info("Get all categories")
    categories.find(BSONDocument()).sort(BSONDocument("name" -> 1)).cursor[Category].collect[Seq]()
  }

  def findById(id: String)(implicit correlationContext: CorrelationContext) = withMdc {
    categories.find(BSONDocument("id" -> id)).cursor[Category].headOption
  }

  def insert(category: Category)(implicit correlationContext: CorrelationContext) = withMdc {
    val toInsert = category.copy(id = Some(UUID.create()))
    categories.insert(toInsert).map(_ => toInsert)
  }

  def update(id: String, category: Category)(implicit correlationContext: CorrelationContext) = withMdc {
    val toUpdate = category.copy(id = Some(id))
    categories.update(BSONDocument("id" -> id), toUpdate).map(_ => toUpdate)
  }

  def deleteById(id: String)(implicit correlationContext: CorrelationContext) = withMdc {
    categories.remove(BSONDocument("id" -> id))
  }
}
