package logging

import org.slf4j.{Logger ⇒ SLFLogger, LoggerFactory ⇒ SLFLoggerFactory}
import org.slf4j.MDC

class CorrelatedLogger(logger: String) {
  val slfLogger = SLFLoggerFactory.getLogger(logger)

  @inline
  final def withMdc(logStatement: => Unit)(implicit correlationContext: CorrelationContext) {
    MDC.put(CorrelatedLogger.SESSION_CORRELATION_ID, correlationContext.sessionCorrelationId)
    MDC.put(CorrelatedLogger.REQUEST_CORRELATION_ID, correlationContext.requestCorrelationId)
    logStatement
    MDC.clear()
  }
}

object CorrelatedLogger {
  final val SESSION_CORRELATION_ID = "X-Session-CorrelationId"
  final val REQUEST_CORRELATION_ID = "X-Request-CorrelationId"
}