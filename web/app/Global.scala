import logging.CorrelationFilter
import play.api.mvc.{RequestHeader, WithFilters}

object Global extends WithFilters(CorrelationFilter) {
  override def onError(request: RequestHeader, ex: Throwable) = super.onError(request, ex)
}
