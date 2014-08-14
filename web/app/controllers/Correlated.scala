package controllers

import play.api.mvc._
import logging.{CorrelatedLogger, CorrelationContext}
import scala.concurrent.Future

class CorrelatedRequest[A](request: Request[A]) extends WrappedRequest[A](request) with CorrelationContext {
  override val sessionCorrelationId = request.tags.getOrElse(CorrelatedLogger.SESSION_CORRELATION_ID,
    throw new RuntimeException(CorrelatedLogger.SESSION_CORRELATION_ID + " not set"))

  override val requestCorrelationId =
    request.tags.getOrElse(CorrelatedLogger.REQUEST_CORRELATION_ID,
      throw new RuntimeException(CorrelatedLogger.SESSION_CORRELATION_ID + " not set"))
}

trait Correlated {
  self: Controller =>

  object CorrelatedAction extends ActionBuilder[CorrelatedRequest] {
    override def invokeBlock[A](request: Request[A], block: (CorrelatedRequest[A]) => Future[Result]) = {
      block(new CorrelatedRequest[A](request))
    }
  }

}
