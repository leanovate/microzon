import logging.{CorrelatedLogging, CorrelationFilter}
import modules.{BackendModule, WebModule}
import play.api.{PlayException, GlobalSettings}
import play.api.mvc.{Handler, RequestHeader}
import scaldi.play.ScaldiSupport

object Global extends GlobalSettings with ScaldiSupport with CorrelatedLogging {

  override def onRequestReceived(request: RequestHeader) = {
    super.onRequestReceived(CorrelationFilter.tagRequest(request))
  }

  override def onError(request: RequestHeader, ex: Throwable) = withMdc(request) {
    log.error( """
                 |
                 |! %sInternal server error, for (%s) [%s] ->
                 | """.stripMargin.format(ex match {
      case p: PlayException => "@" + p.id + " - "
      case _ => ""
    }, request.method, request.uri), ex)
    super.onError(request, ex)
  }

  override def applicationModule = new WebModule :: new BackendModule

}
