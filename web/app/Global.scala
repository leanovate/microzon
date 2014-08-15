import logging.{CorrelatedLogging, CorrelationFilter}
import modules.{BackendModule, WebModule}
import play.api.GlobalSettings
import play.api.mvc.{Handler, RequestHeader}
import scaldi.play.ScaldiSupport

object Global extends GlobalSettings with ScaldiSupport with CorrelatedLogging {

  override def onRequestReceived(request: RequestHeader) = {
    super.onRequestReceived(CorrelationFilter.tagRequest(request))
  }

  override def onError(request: RequestHeader, ex: Throwable) = withMdc(request) {
    log.error("Application error", ex)
    super.onError(request, ex)
  }

  override def applicationModule = new WebModule :: new BackendModule

}
