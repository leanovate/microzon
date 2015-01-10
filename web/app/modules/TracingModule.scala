package modules

import scaldi.Module
import com.github.kristofa.brave.SpanCollector
import com.github.kristofa.brave.zipkin.ZipkinSpanCollector
import play.api.Play
import play.api.Play.current
import com.twitter.zipkin.gen.Span
import akka.event.slf4j.SLF4JLogging

import scala.util.{Failure, Success, Try}

class TracingModule extends Module with SLF4JLogging {
  val zipkinHost = Play.configuration.getString("zipkin.host").getOrElse(throw new RuntimeException("No zipkin.host"))
  val zipkinPort = Play.configuration.getInt("zipkin.port").getOrElse(throw new RuntimeException("No zipkin.port"))

  bind[SpanCollector] to (Try(new ZipkinSpanCollector(zipkinHost, zipkinPort)) match {
    case Success(zipkinCollector) => zipkinCollector
    case Failure(e) =>
      log.error("Failed to setup zipkin collector", e)
      new SpanCollector {
        override def collect(span: Span) {}

        override def close() {}

        override def addDefaultAnnotation(key: String, value: String) {}
      }
  })
}
