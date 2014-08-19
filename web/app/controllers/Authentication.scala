package controllers

import play.api.mvc._
import logging.{CorrelatedLogging, CorrelationContext}
import scala.concurrent.Future
import models.UserContext
import play.api.libs.concurrent.Execution.Implicits._
import backend.CustomerBackend
import models.user.Customer

class ContextRequest[A](request: Request[A],
                        correlationContext: CorrelationContext,
                        var customer: Option[Customer]) extends WrappedRequest[A](request) with CorrelationContext with UserContext {
  override val correlationId = correlationContext.correlationId

  override def isAuthenticated = customer.isDefined
}

trait Authentication {
  self: Controller =>

  def customerBackend: CustomerBackend

  def createContextRequest[A](request: Request[A]) = {
    implicit val correlationContext = new CorrelationContext {
      override val correlationId = request.tags.getOrElse(CorrelatedLogging.CORRELATION_ID,
        throw new RuntimeException(CorrelatedLogging.CORRELATION_ID + " not set"))
    }

    request.session.get("customerId").map(_.toLong).fold {
      Future.successful(new ContextRequest(request, correlationContext, None))
    } {
      customerId =>
        customerBackend.getCustomer(customerId).map {
          customer => new ContextRequest(request, correlationContext, Some(customer))
        }
    }
  }

  object UnauthenticatedAction extends ActionBuilder[ContextRequest] {

    override def invokeBlock[A](request: Request[A], block: (ContextRequest[A]) => Future[Result]) = {

      createContextRequest(request).flatMap {
        contextRequest =>
          block(contextRequest)
      }

    }
  }

  object AuthenticatedAction extends ActionBuilder[ContextRequest] {
    override def invokeBlock[A](request: Request[A], block: (ContextRequest[A]) => Future[Result]) = {
      createContextRequest(request).flatMap {
        contextRequest =>
          if (!contextRequest.isAuthenticated)
            onUnauthenticated(contextRequest)
          else
            block(contextRequest)
      }
    }
  }

  def onUnauthenticated[A](request: ContextRequest[A]): Future[Result] = {
    Future.successful(Redirect(routes.UserLoginController.showForm()))
  }
}
