package modules

import backend.ConsulLookup
import com.github.kristofa.brave.SpanCollector
import com.github.kristofa.brave.zipkin.ZipkinSpanCollector
import com.twitter.zipkin.gen.Span
import play.api.Logger
import scaldi.{Injectable, Injector}
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.duration._
import scala.concurrent.Await
import scala.util.{Failure, Success, Try}


class SpanCollectorFactory(implicit inj: Injector) extends Injectable {
  private val consulLookup = inject[ConsulLookup]
  private val serviceName = "zipkin-collector"

  def provide: SpanCollector = {
    val futureCollector = consulLookup.lookup(serviceName).map {
      serviceNodes =>
        if (serviceNodes.isEmpty) {
          Logger.info(s"No active zipkin collector found")
          fallback
        } else {
          val firstNode = serviceNodes.head
          Try(new ZipkinSpanCollector(firstNode.Address, firstNode.ServicePort)) match {
            case Success(zipkinCollector) => zipkinCollector
            case Failure(e) =>
              Logger.error("Failed to setup zipkin collector", e)
              fallback
          }
        }
    }

    Await.result(futureCollector, 10.seconds)
  }

  def fallback: SpanCollector = new SpanCollector {
    override def collect(span: Span) {}

    override def close() {}

    override def addDefaultAnnotation(key: String, value: String) {}
  }
}
