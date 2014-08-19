package logging

import play.api.libs.ws.WS
import play.api.Play.current

object CorrelatedWS extends CorrelatedLogging {
  def url(url: String)(implicit correlationContext: CorrelationContext) = withMdc {
    log.info("Prepare http request: {}", url)
    WS.url(url).withHeaders(
      CorrelatedLogging.CORRELATION_ID -> correlationContext.correlationId
    )
  }
}
