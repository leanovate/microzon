package de.leanovate.dose.product.logging

import scala.concurrent.{ExecutionContext, Future}
import org.slf4j.MDC

class ContextFuture[T](wrapped: Future[T]) {
  def mapWithMdc[S](f: T => S)(implicit executor: ExecutionContext) = {
    val contextMap = MDC.getCopyOfContextMap
    wrapped.map {
      result =>
        if (contextMap ne null)
          MDC.setContextMap(contextMap)
        f(result)
    }
  }
}

object ContextFuture {
  implicit def future2ContextFuture[T](future: Future[T]): ContextFuture[T] = new ContextFuture[T](future)
}