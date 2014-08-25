package logging

import com.twitter.zipkin.gen.{zipkinCoreConstants, Endpoint, Annotation, Span}
import scala.util.Random
import java.net.InetAddress
import java.nio.ByteBuffer
import com.github.kristofa.brave.SpanCollector

trait TraceData {
  def traceId: Long

  def spanId: Long

  def parentSpanId: Option[Long]

  def shouldBeSampled: Boolean

  def spanName: String

  val span = {
    val result = new Span()
    result.setTrace_id(traceId)
    result.setId(spanId)
    parentSpanId.foreach(result.setParent_id)
    result.setName(spanName)

    result
  }

  def submitAnnotation(annotationName: String, endpoint: Endpoint) {
    if (shouldBeSampled) {
      val annotation = new Annotation

      annotation.setTimestamp(currentTimeMicroseconds)
      annotation.setHost(endpoint)
      annotation.setValue(annotationName)
      span.addToAnnotations(annotation)
    }
  }

  def toCollector(collector: SpanCollector) {
    if (shouldBeSampled)
      collector.collect(span)
  }

  private def currentTimeMicroseconds: Long = System.currentTimeMillis * 1000
}

case class ServerTraceData(traceId: Long,
                           spanId: Long,
                           parentSpanId: Option[Long],
                           shouldBeSampled: Boolean,
                           spanName: String)
  extends TraceData {

  def serverReceived() {
    submitAnnotation(zipkinCoreConstants.SERVER_RECV, TraceData.serverEndpoint)
  }

  def serverSend() {
    submitAnnotation(zipkinCoreConstants.SERVER_SEND, TraceData.serverEndpoint)
  }

  def createClient(host: String, port: Short, serviceName: String, spanName: String) = {
    val endpoint = new Endpoint(TraceData.serverEndpoint)
    endpoint.setService_name(serviceName)
    ClientTraceData(traceId, TraceData.generateId, Some(spanId), shouldBeSampled, spanName, endpoint)
  }
}

case class ClientTraceData(traceId: Long,
                           spanId: Long,
                           parentSpanId: Option[Long],
                           shouldBeSampled: Boolean,
                           spanName: String,
                           endpoint: Endpoint)
  extends TraceData {

  def clientSend() {
    submitAnnotation(zipkinCoreConstants.CLIENT_SEND, endpoint)
  }

  def clientReceived() {
    submitAnnotation(zipkinCoreConstants.CLIENT_RECV, endpoint)
  }
}

object TraceData {
  val random = new Random()

  def generateId: Long = random.nextLong() & Long.MaxValue

  lazy val serverEndpoint =
    new Endpoint(ByteBuffer.wrap(InetAddress.getLocalHost.getAddress).get, 80, "Web")
}