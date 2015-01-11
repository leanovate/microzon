package backend

import logging.{CorrelatedLogging, CorrelatedWS, CorrelationContext}
import models.billing.CreateOrder
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import scaldi.{Injectable, Injector}

class BillingBackend(implicit inj: Injector) extends Injectable with CorrelatedLogging {
  private val correlatedWS = inject[CorrelatedWS]
  private val serviceFailover = inject[ServiceFailover]

  import backend.BillingBackend._

  def createOrder(customerId: Long, cartId: String)(implicit collectionContext: CorrelationContext) =
    serviceFailover.retry(serviceName, "/orders") {
      url =>
        val createOrder = CreateOrder(customerId, cartId)
        correlatedWS.post(serviceName, url, Json.toJson(createOrder)).map {
          response =>
            if (response.status != 200)
              throw new RuntimeException(s"post create order to billing service failed status=${response.status}")
            response
        }
    }
}

object BillingBackend {
  val serviceName = "billing-service"
}