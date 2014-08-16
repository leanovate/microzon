package logging

import org.slf4j.{Logger ⇒ SLFLogger, LoggerFactory ⇒ SLFLoggerFactory}
import org.slf4j.MDC
import akka.event.slf4j.SLF4JLogging
import play.api.mvc.RequestHeader

trait CorrelatedLogging extends SLF4JLogging {
  @inline
  final def withMdc[A](block: => A)(implicit correlationContext: CorrelationContext): A = {
    MDC.put(CorrelatedLogging.SESSION_CORRELATION_ID, correlationContext.sessionCorrelationId)
    MDC.put(CorrelatedLogging.REQUEST_CORRELATION_ID, correlationContext.requestCorrelationId)
    val result = block
    MDC.clear()
    result
  }

  @inline
  final def withMdc[A](request: RequestHeader)(block: => A): A = {
    request.tags.get(CorrelatedLogging.SESSION_CORRELATION_ID).foreach {
      sessionCorrelationId =>
        MDC.put(CorrelatedLogging.SESSION_CORRELATION_ID, sessionCorrelationId)
    }
    request.tags.get(CorrelatedLogging.REQUEST_CORRELATION_ID).foreach {
      requestCorrelationId =>
        MDC.put(CorrelatedLogging.REQUEST_CORRELATION_ID, requestCorrelationId)
    }
    val result = block
    MDC.clear()
    result
  }
}

object CorrelatedLogging {
  final val SESSION_CORRELATION_COOKIE = "correlationid"
  final val SESSION_CORRELATION_ID = "X-Session-CorrelationId"
  final val REQUEST_CORRELATION_ID = "X-Request-CorrelationId"
}