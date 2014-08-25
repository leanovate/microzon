package logging

import play.api.libs.ws.{WSResponse, WS}
import play.api.Play.current
import scala.concurrent.Future
import play.api.http.{ContentTypeOf, Writeable}
import scaldi.{Injectable, Injector}
import java.net.URL
import com.github.kristofa.brave.{SpanCollector, BraveHttpHeaders}
import play.api.libs.concurrent.Execution.Implicits._

class CorrelatedWS(implicit inj: Injector) extends CorrelatedLogging with Injectable {

  val spanCollector = inject[SpanCollector]

  def get(serviceName: String, url: String)(implicit correlationContext: CorrelationContext): Future[WSResponse] = withMdc {

    log.info("Perform GET {} on {} correlationId={}", url, serviceName, correlationContext.correlationId)
    val clientTrace = createClientTrace(serviceName, "GET", url)
    clientTrace.clientSend()
    val response = WS.url(url).withHeaders(
      (CorrelatedLogging.CORRELATION_ID -> correlationContext.correlationId) +: traceHeaders(clientTrace): _*
    ).get()
    response.onComplete {
      _ =>
        clientTrace.clientReceived()
        clientTrace.toCollector(spanCollector)
    }
    response
  }

  def post[T](serviceName: String, url: String, body: T)(implicit correlationContext: CorrelationContext,
                                                         wrt: Writeable[T], ct: ContentTypeOf[T]): Future[WSResponse] = withMdc {
    log.info("Perform POST {} on {} correlationId={}", url, serviceName, correlationContext.correlationId)
    val clientTrace = createClientTrace(serviceName, "POST", url)
    clientTrace.clientSend()
    val response = WS.url(url).withHeaders(
      (CorrelatedLogging.CORRELATION_ID -> correlationContext.correlationId) +: traceHeaders(clientTrace): _*
    ).post(body)
    response.onComplete {
      _ =>
        clientTrace.clientReceived()
        clientTrace.toCollector(spanCollector)
    }
    response
  }

  def put[T](serviceName: String, url: String, body: T)(implicit correlationContext: CorrelationContext,
                                                        wrt: Writeable[T], ct: ContentTypeOf[T]): Future[WSResponse] = withMdc {
    log.info("Perform PUT {} on {} correlationId={}", url, serviceName, correlationContext.correlationId)
    val clientTrace = createClientTrace(serviceName, "PUT", url)
    clientTrace.clientSend()
    val response = WS.url(url).withHeaders(
      (CorrelatedLogging.CORRELATION_ID -> correlationContext.correlationId) +: traceHeaders(clientTrace): _*
    ).put(body)
    response.onComplete {
      _ =>
        clientTrace.clientReceived()
        clientTrace.toCollector(spanCollector)
    }
    response
  }

  def delete(serviceName: String, url: String)(implicit correlationContext: CorrelationContext): Future[WSResponse] = withMdc {
    log.info("Perform DELETE {} on {} correlationId={}", url, serviceName, correlationContext.correlationId)
    val clientTrace = createClientTrace(serviceName, "DELETE", url)
    clientTrace.clientSend()
    val response = WS.url(url).withHeaders(
      (CorrelatedLogging.CORRELATION_ID -> correlationContext.correlationId) +: traceHeaders(clientTrace): _*
    ).delete()
    response.onComplete {
      _ =>
        clientTrace.clientReceived()
        clientTrace.toCollector(spanCollector)
    }
    response
  }

  private def createClientTrace(serviceName: String, method: String, urlString: String)(implicit correlationContext: CorrelationContext) = {
    val url = new URL(urlString)

    correlationContext.traceData.createClient(url.getHost, if (url.getPort < 0) 80 else url.getPort.toShort, serviceName, method + " " + url.getPath)
  }

  private def traceHeaders(traceData: TraceData): Seq[(String, String)] = Seq(
    BraveHttpHeaders.TraceId.getName -> java.lang.Long.toString(traceData.traceId, 16),
    BraveHttpHeaders.SpanId.getName -> java.lang.Long.toString(traceData.spanId, 16),
    BraveHttpHeaders.Sampled.getName -> traceData.shouldBeSampled.toString,
    BraveHttpHeaders.SpanName.getName -> traceData.spanName
  ) ++ traceData.parentSpanId.map(BraveHttpHeaders.ParentSpanId.getName -> java.lang.Long.toString(_, 16)).toSeq
}
