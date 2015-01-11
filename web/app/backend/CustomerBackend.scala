package backend

import logging.{CorrelatedLogging, CorrelatedWS, CorrelationContext}
import models.user._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import scaldi.{Injectable, Injector}

class CustomerBackend(implicit inj: Injector) extends Injectable with CorrelatedLogging {
  private val correlatedWS = inject[CorrelatedWS]
  private val serviceFailover = inject[ServiceFailover]

  import backend.CustomerBackend._

  def login(login: Login)(implicit collectionContext: CorrelationContext) =
    serviceFailover.retry(serviceName, "/login") {
      url =>
        correlatedWS.post(serviceName, url, Json.toJson(login)).map {
          response =>
            if (response.status != 200)
              throw new RuntimeException(s"Login via customer service failed status=${response.status}")
            response.json.as[LoginResult]
        }
    }

  def registerCustomer(registration: Registration)(implicit collectionContext: CorrelationContext) =
    serviceFailover.retry(serviceName, "/registration") {
      url =>
        correlatedWS.post(serviceName, url, Json.toJson(registration)).map {
          response =>
            if (response.status != 200)
              throw new RuntimeException(s"registration via customer service failed status=${response.status}")
            response.json.as[RegistrationResult]
        }
    }

  def getCustomer(customerId: Long)(implicit collectionContext: CorrelationContext) =
    serviceFailover.retry(serviceName, "/customers/" + customerId) {
      url =>
        correlatedWS.get(serviceName, url).map {
          response =>
            if (response.status != 200)
              throw new RuntimeException(s"get customer via customer service failed status=${response.status}")
            response.json.as[Customer]

        }
    }
}

object CustomerBackend {
  val serviceName = "customer-service"
}
