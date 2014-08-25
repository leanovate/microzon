package modules

import scaldi.Module
import com.github.kristofa.brave.SpanCollector
import com.github.kristofa.brave.zipkin.ZipkinSpanCollector
import play.api.Play
import play.api.Play.current
import com.twitter.zipkin.gen.Span

class TracingModule extends Module {
  val zipkinHost = Play.configuration.getString("zipkin.host").getOrElse(throw new RuntimeException("No zipkin.host"))
  val zipkinPort = Play.configuration.getInt("zipkin.port").getOrElse(throw new RuntimeException("No zipkin.port"))

  bind[SpanCollector] to new ZipkinSpanCollector(zipkinHost, zipkinPort)
//  bind[SpanCollector] to new SpanCollector {
//    override def collect(span: Span) = {
//      println(span)
//    }
//
//    override def close() = {
//
//    }
//
//    override def addDefaultAnnotation(key: String, value: String) = {
//
//    }
//  }
}
