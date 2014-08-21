package de.leanovate.dose.product.logging

import tyrex.services.UUID
import shapeless.{::, HNil}
import spray.routing.{Route, Directive1}

object CorrelatedRouting extends CorrelatedLogging {
  val correlationContext = new Directive1[CorrelationContext] {
    override def happly(inner: (CorrelationContext :: HNil) => Route) = {
      ctx =>
        implicit val correlctionContext: CorrelationContext = new CorrelationContext {
          override val correlationId = ctx.request.headers.find(_.lowercaseName == "x-correlationid").map(_.value).getOrElse(UUID.create())
        }
        withMdc {
          inner(correlctionContext :: HNil)(ctx)
        }
    }
  }
}
