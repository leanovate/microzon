package de.leanovate.dose.product.logging

import shapeless.{::, HNil}
import spray.routing.{Directive, Directive0, Route, Directive1}
import reactivemongo.bson.BSONObjectID
import org.slf4j.MDC
import spray.routing.directives.OnSuccessFutureMagnet

object CorrelatedRouting {
  final val CORRELATION_ID = "X-CorrelationId"

  val correlationContext = new Directive0 {
    override def happly(inner: (HNil) => Route) = {
      ctx =>
        MDC.put(CORRELATION_ID, ctx.request.headers.find(_.lowercaseName == "x-correlationid").map(_.value).getOrElse(BSONObjectID.generate.stringify))

        inner(HNil)(ctx)
    }
  }

  def onSuccessWithMdc(magnet: OnSuccessFutureMagnet): Directive[magnet.Out] = {
    new Directive[magnet.Out] {
      override def happly(f: (magnet.Out) => Route) = {
        ctx =>
          val contextMap = MDC.getCopyOfContextMap
          println(contextMap)

          magnet.get.happly {
            out =>
              if (contextMap ne null)
                MDC.setContextMap(contextMap)
              f(out)
          }(ctx)
      }
    }
  }
}
