package de.leanovate.dose.product.repository

import reactivemongo.api.MongoDriver
import de.leanovate.dose.product.Akka

object Mongo {

  import Akka._

  val driver = new MongoDriver(actorSystem)
  val connection = driver.connection(List("localhost"))
  val productsDb = connection.db("products")
}