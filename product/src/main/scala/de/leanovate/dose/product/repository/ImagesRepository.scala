package de.leanovate.dose.product.repository

import reactivemongo.api.gridfs.GridFS
import reactivemongo.bson.BSONDocument
import de.leanovate.dose.product.Akka
import akka.event.slf4j.SLF4JLogging

object ImagesRepository extends SLF4JLogging {

  import reactivemongo.api.gridfs.Implicits._
  import Akka._

  val images = GridFS(Mongo.productsDb, prefix = "images")

  def findAll() = {
    log.info("Get all images")
    images.find(BSONDocument()).collect[Seq]()
  }
}
