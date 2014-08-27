import logging.{CorrelatedLogging, CorrelationFilter}
import modules.{TracingModule, BackendModule, WebModule}
import org.slf4j.MDC
import play.api.{PlayException, GlobalSettings}
import play.api.mvc.{Handler, RequestHeader}
import scaldi.play.ScaldiSupport

object Global extends GlobalSettings with ScaldiSupport with CorrelatedLogging {

  override def onRequestReceived(request: RequestHeader) = {
    // Little hack to ensure that potentially generated correlation id is available in error handler
    super.onRequestReceived(CorrelationFilter.tagRequest(request))
  }

  override def onError(request: RequestHeader, ex: Throwable) = withMdc(request) {
    val id = ex match {
      case p: PlayException => "@" + p.id + " - "
      case _ => ""
    }
    log.error( """
                 |
                 |! %sInternal server error, for (%s) [%s] ->
                 | """.stripMargin.format(id, request.method, request.uri), ex)
    log.error( s"Error page shown with id=$id in correlationId=${MDC.get(CorrelatedLogging.CORRELATION_ID)}")
    super.onError(request, ex)
  }

  override def applicationModule = new WebModule :: new BackendModule :: new TracingModule

}
