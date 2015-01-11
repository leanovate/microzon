package modules

import akka.event.slf4j.SLF4JLogging
import com.github.kristofa.brave.SpanCollector
import scaldi.Module

class TracingModule extends Module with SLF4JLogging {
  bind[SpanCollector] to new SpanCollectorFactory().provide

}
