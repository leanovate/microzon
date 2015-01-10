package backend

import logging.CorrelatedLogging
import scaldi.{Injectable, Injector}

import scala.collection.concurrent

class ConsulLookup(implicit inj: Injector) extends Injectable with CorrelatedLogging {
  private val baseUrl = inject[String](identified by "consul.lookup.url")
  private val serviceLookups = concurrent.TrieMap.empty[String, ConsulServiceLookup]

  def lookup(serviceName: String) =
    serviceLookups.getOrElseUpdate(serviceName, new ConsulServiceLookup(baseUrl, serviceName)).lookup
}
