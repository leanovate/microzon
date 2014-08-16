package controllers

import play.api.mvc._
import logging.{CorrelatedLogging, CorrelationContext}
import scala.concurrent.Future
import models.UserContext
import play.api.libs.concurrent.Execution.Implicits._

class ContextRequest[A](request: Request[A]) extends WrappedRequest[A](request) with CorrelationContext with UserContext {
  override val sessionCorrelationId = request.tags.getOrElse(CorrelatedLogging.SESSION_CORRELATION_ID,
    throw new RuntimeException(CorrelatedLogging.SESSION_CORRELATION_ID + " not set"))

  override val requestCorrelationId =
    request.tags.getOrElse(CorrelatedLogging.REQUEST_CORRELATION_ID,
      throw new RuntimeException(CorrelatedLogging.SESSION_CORRELATION_ID + " not set"))

  override def customerId = request.session.get("customerId").map(_.toLong)

  override def isAuthenticated = customerId.isDefined
}

trait Authentication {
  self: Controller =>

  object UnauthenticatedAction extends ActionBuilder[ContextRequest] {
    override def invokeBlock[A](request: Request[A], block: (ContextRequest[A]) => Future[Result]) = {
      val contextRequest = new ContextRequest[A](request)

      block(contextRequest).map(_.withCookies(Cookie(CorrelatedLogging.SESSION_CORRELATION_COOKIE, contextRequest.sessionCorrelationId)))
    }
  }

  object AuthenticatedAction extends ActionBuilder[ContextRequest] {
    override def invokeBlock[A](request: Request[A], block: (ContextRequest[A]) => Future[Result]) = {
      val contextRequest = new ContextRequest[A](request)

      if (!contextRequest.isAuthenticated)
        onUnauthenticated(contextRequest)
      else
        block(contextRequest).map(_.withCookies(Cookie(CorrelatedLogging.SESSION_CORRELATION_COOKIE, contextRequest.sessionCorrelationId)))
    }
  }

  def onUnauthenticated[A](request: ContextRequest[A]): Future[Result] = {
    Future.successful(Redirect(routes.UserLoginController.showForm()))
  }
}
