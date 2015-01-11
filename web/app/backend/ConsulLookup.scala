package backend

import logging.CorrelatedLogging
import models.consul.ServiceNode
import play.api.libs.concurrent.Execution.Implicits._
import scaldi.{Injectable, Injector}

import scala.collection.concurrent
import scala.concurrent.Future

class ConsulLookup(implicit inj: Injector) extends Injectable with CorrelatedLogging {
  private val baseUrl = inject[String](identified by "consul.lookup.url")
  private val serviceLookups = concurrent.TrieMap.empty[String, ConsulServiceLookup]

  def lookup(serviceName: String) =
    serviceLookups.getOrElseUpdate(serviceName, new ConsulServiceLookup(baseUrl, serviceName)).lookup

  def lookupAll(serviceNames: Seq[String]): Future[Seq[(String, Seq[ServiceNode])]] =
    Future.traverse(serviceNames) {
      serviceName =>
        lookup(serviceName).map {
          serviceNodes =>
            serviceName -> serviceNodes
        }
    }
}
