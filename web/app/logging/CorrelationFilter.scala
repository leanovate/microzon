package logging

import play.api.mvc.{Cookie, Result, RequestHeader, Filter}
import scala.concurrent.Future
import tyrex.services.UUID
import play.api.libs.concurrent.Execution.Implicits._
import org.slf4j.MDC

object CorrelationFilter {
  def tagRequest(rh: RequestHeader) = {
    val correlationId = rh.headers.get(CorrelatedLogging.CORRELATION_ID).getOrElse(UUID.create())

    val correlationTags = Map(
      CorrelatedLogging.CORRELATION_ID -> correlationId
    )

    rh.copy(tags = rh.tags ++ correlationTags)
  }
}
