package de.leanovate.dose.product.repository

import akka.event.slf4j.SLF4JLogging
import de.leanovate.dose.product.consul.ConsulLookup
import reactivemongo.api.{MongoConnection, MongoDriver}

import scala.concurrent.Future

object Mongo extends SLF4JLogging {

  import de.leanovate.dose.product.Akka._

  val driver = new MongoDriver(actorSystem)
  val connection: Future[MongoConnection] =
    ConsulLookup.lookup("mongo").map {
      serviceNodes =>
        val endpoints = serviceNodes.map(s => s"${s.Address}:${s.ServicePort}")
        log.info(s"Using mongo endpoints: $endpoints")
        driver.connection(endpoints)
    }

  val productsDb = connection.map(_.db("products"))
}