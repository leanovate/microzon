package backend

import scaldi.{Injectable, Injector}
import logging.CorrelatedLogging

class CartBackend(implicit inj: Injector) extends Injectable with CorrelatedLogging {
  private val baseUrl = inject[String](identified by "service.cart.url")

}
