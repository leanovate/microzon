package backend

import scaldi.{Injectable, Injector}
import models.user._
import logging.{CorrelatedLogging, CorrelatedWS, CorrelationContext}
import play.api.libs.json.Json
import play.api.libs.concurrent.Execution.Implicits._

class CustomerBackend(implicit inj: Injector) extends Injectable with CorrelatedLogging {
  private val baseUrl = inject[String](identified by "service.customer.url")

  def login(login: Login)(implicit collectionContext: CorrelationContext) = {
    CorrelatedWS.url(baseUrl + "/login").post(Json.toJson(login)).map {
      response =>
        if (response.status != 200)
          throw new RuntimeException(s"Login via customer service failed status=${response.status}")
        response.json.as[LoginResult]
    }
  }

  def registerCustomer(registration: Registration)(implicit collectionContext: CorrelationContext) = {
    CorrelatedWS.url(baseUrl + "/registration").post(Json.toJson(registration)).map {
      response =>
        if (response.status != 200)
          throw new RuntimeException(s"Login via customer service failed status=${response.status}")
        response.json.as[RegistrationResult]
    }
  }

  def getCustomer(customerId:Long)(implicit collectionContext: CorrelationContext) = {
    CorrelatedWS.url(baseUrl + "/customers/" + customerId).get().map {
      response =>
        if (response.status != 200)
          throw new RuntimeException(s"Login via customer service failed status=${response.status}")
        response.json.as[Customer]

    }
  }
}
