package de.leanovate.dose.product.repository

import de.leanovate.dose.product.Akka
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.{BSONObjectID, BSONDocument}
import de.leanovate.dose.product.model.Category
import de.leanovate.dose.product.logging.{CorrelationContext, CorrelatedLogging}

object CategoryRepository extends CorrelatedLogging {

  import Akka._

  val categories = Mongo.productsDb.collection[BSONCollection]("categories")

  def findAll()(implicit correlationContext: CorrelationContext) = withMdc {
    log.info("Get all categories")
    categories.find(BSONDocument()).sort(BSONDocument("name" -> 1)).cursor[Category].collect[Seq]()
  }

  def findById(id: String)(implicit correlationContext: CorrelationContext) = withMdc {
    categories.find(BSONDocument("_id" -> BSONObjectID(id))).cursor[Category].headOption
  }

  def insert(category: Category)(implicit correlationContext: CorrelationContext) = withMdc {
    val toInsert = category.copy(_id = Some(BSONObjectID.generate))
    categories.insert(toInsert).map(_ => toInsert)
  }

  def update(id: String, category: Category)(implicit correlationContext: CorrelationContext) = withMdc {
    val toUpdate = category.copy(_id = Some(BSONObjectID(id)))
    categories.update(BSONDocument("_id" -> BSONObjectID(id)), toUpdate).map(_ => toUpdate)
  }

  def deleteById(id: String)(implicit correlationContext: CorrelationContext) = withMdc {
    categories.remove(BSONDocument("_id" -> BSONObjectID(id)))
  }
}
