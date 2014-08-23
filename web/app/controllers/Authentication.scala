package controllers

import play.api.mvc._
import logging.{CorrelatedLogging, CorrelationContext}
import scaldi.Injectable
import scala.concurrent.Future
import models.UserContext
import play.api.libs.concurrent.Execution.Implicits._
import backend.{CartBackend, CustomerBackend}
import models.user.Customer
import models.cart.Cart

class ContextRequest[A](request: Request[A],
                        correlationContext: CorrelationContext,
                        val customer: Option[Customer],
                        val cart: Option[Cart]) extends WrappedRequest[A](request) with CorrelationContext with UserContext {
  override val correlationId = correlationContext.correlationId

  override def isAuthenticated = customer.isDefined
}

trait Authentication {
  self: Controller =>

  def customerBackend: CustomerBackend

  def cartBackend: CartBackend

  def createContextRequest[A](request: Request[A]) = {
    implicit val correlationContext = new CorrelationContext {
      override val correlationId = request.tags.getOrElse(CorrelatedLogging.CORRELATION_ID,
        throw new RuntimeException(CorrelatedLogging.CORRELATION_ID + " not set"))
    }

    val customerFuture = request.session.get("customerId").map(_.toLong).map {
      customerId =>
        customerBackend.getCustomer(customerId).map(Some.apply)
    }.getOrElse((Future.successful(None)))
    val cartFuture = request.session.get("cartId").map {
      cartId =>
        cartBackend.getCart(cartId).map(Some.apply)
    }.getOrElse(Future.successful(None))
    customerFuture.zip(cartFuture).map {
      case (customer, cart) =>
        new ContextRequest(request, correlationContext, customer, cart)
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
