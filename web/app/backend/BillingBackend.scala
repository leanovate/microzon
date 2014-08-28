package backend

import scaldi.{Injectable, Injector}
import logging.{CorrelationContext, CorrelatedWS, CorrelatedLogging}
import models.billing.CreateOrder
import play.api.libs.json.Json
import play.api.libs.concurrent.Execution.Implicits._

class BillingBackend(implicit inj: Injector) extends Injectable with CorrelatedLogging {
  private val correlatedWS = inject[CorrelatedWS]
  private val baseUrl = inject[String](identified by "service.billing.url")
  private val name = "Billing"

  def createOrder(customerId: Long, cartId: String)(implicit collectionContext: CorrelationContext) = {
    val createOrder = CreateOrder(customerId, cartId)
    correlatedWS.post(name, baseUrl + "/orders", Json.toJson(createOrder)).map {
      response =>
        if (response.status != 200)
          throw new RuntimeException(s"post create order to billing service failed status=${response.status}")
        response
    }
  }
}
