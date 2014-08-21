package de.leanovate.dose.product.repository

import de.leanovate.dose.product.logging.{CorrelationContext, CorrelatedLogging}
import reactivemongo.api.gridfs.GridFS
import reactivemongo.bson.BSONDocument
import de.leanovate.dose.product.Akka

object ImagesRepository extends CorrelatedLogging {

  import reactivemongo.api.gridfs.Implicits._
  import Akka._

  val images = GridFS(Mongo.productsDb, prefix = "images")

  def findAll()(implicit correlationContext: CorrelationContext) = withMdc {
    log.info("Get all images")
    images.find(BSONDocument()).collect[Seq]()
  }
}
