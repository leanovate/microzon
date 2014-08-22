package de.leanovate.dose.cart.logging

import org.jboss.netty.handler.codec.http.HttpRequest
import com.twitter.finagle.{Service, SimpleFilter}

class CorrelationHttpFilter[Req <: HttpRequest, Res] extends SimpleFilter[Req, Res] {

  val correlationHeader = "X-CorrelationId"

  override def apply(request: Req, service: Service[Req, Res]) = {
    if (request.headers.contains(correlationHeader)) {
      CorrelationContext.setCorrelationId(request.headers.get("X-CorrelationId"))
    }
    service.apply(request)
  }
}
