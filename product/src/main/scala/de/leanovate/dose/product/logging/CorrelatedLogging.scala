package de.leanovate.dose.product.logging

import akka.event.slf4j.SLF4JLogging
import org.slf4j.MDC

trait CorrelatedLogging extends SLF4JLogging {
  @inline
  final def withMdc[A](block: => A)(implicit correlationContext: CorrelationContext): A = {
    MDC.put(CorrelatedLogging.CORRELATION_ID, correlationContext.correlationId)
    val result = block
    MDC.clear()
    result
  }

}

object CorrelatedLogging {
  final val CORRELATION_ID = "X-CorrelationId"
}
