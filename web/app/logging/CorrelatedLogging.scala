package logging

import org.slf4j.{Logger ⇒ SLFLogger, LoggerFactory ⇒ SLFLoggerFactory}
import org.slf4j.MDC
import akka.event.slf4j.SLF4JLogging
import play.api.mvc.RequestHeader

trait CorrelatedLogging extends SLF4JLogging {
  @inline
  final def withMdc[A](block: => A)(implicit correlationContext: CorrelationContext): A = {
    MDC.put(CorrelatedLogging.CORRELATION_ID, correlationContext.correlationId)
    val result = block
    MDC.clear()
    result
  }

  @inline
  final def withMdc[A](request: RequestHeader)(block: => A): A = {
    request.tags.get(CorrelatedLogging.CORRELATION_ID).foreach {
      sessionCorrelationId =>
        MDC.put(CorrelatedLogging.CORRELATION_ID, sessionCorrelationId)
    }
    val result = block
    MDC.clear()
    result
  }
}

object CorrelatedLogging {
  final val CORRELATION_COOKIE = "correlationid"
  final val CORRELATION_ID = "X-CorrelationId"
}