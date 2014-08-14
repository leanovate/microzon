package logging

import play.api.mvc.{Cookie, Result, RequestHeader, Filter}
import scala.concurrent.Future
import tyrex.services.UUID
import play.api.libs.concurrent.Execution.Implicits._

object CorrelationFilter extends Filter {
  override def apply(f: (RequestHeader) => Future[Result])(rh: RequestHeader) = {
    val optSessionCorrelationId = rh.cookies.get(CorrelatedLogger.SESSION_CORRELATION_ID).map(_.value)

    val sessionCorrelationId = optSessionCorrelationId.getOrElse(
      rh.headers.get(CorrelatedLogger.SESSION_CORRELATION_ID).getOrElse(UUID.create())
    )

    val requestCorrelationId =
      rh.headers.get(CorrelatedLogger.REQUEST_CORRELATION_ID).getOrElse(UUID.create())

    val correlationTags = Map(
      CorrelatedLogger.SESSION_CORRELATION_ID -> sessionCorrelationId,
      CorrelatedLogger.REQUEST_CORRELATION_ID -> requestCorrelationId
    )

    val result = f(rh.copy(tags = rh.tags ++ correlationTags))

    if (optSessionCorrelationId.isEmpty) {
      val correlationCookie = Cookie(CorrelatedLogger.SESSION_CORRELATION_ID, sessionCorrelationId)
      result.map(_.withCookies(correlationCookie))
    } else {
      result
    }
  }
}
