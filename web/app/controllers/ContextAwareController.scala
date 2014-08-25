package controllers

import play.api.mvc._
import logging.{ServerTraceData, TraceData, CorrelatedLogging, CorrelationContext}
import scaldi.{Injector, Injectable}
import scala.concurrent.Future
import models.UserContext
import play.api.libs.concurrent.Execution.Implicits._
import backend.{CartBackend, CustomerBackend}
import models.user.Customer
import models.cart.Cart
import com.github.kristofa.brave.{SpanCollector, BraveHttpHeaders}
import scala.util.{Failure, Success}

class ContextRequest[A](request: Request[A],
                        correlationContext: CorrelationContext,
                        val customer: Option[Customer],
                        val cart: Option[Cart]) extends WrappedRequest[A](request) with CorrelationContext with UserContext {
  override val correlationId = correlationContext.correlationId

  override val traceData = correlationContext.traceData

  override def isAuthenticated = customer.isDefined
}

abstract class ContextAwareController(implicit inj: Injector) extends Controller with Injectable {

  val customerBackend = inject[CustomerBackend]
  val cartBackend = inject[CartBackend]
  val spanCollector = inject[SpanCollector]

  def createContextRequest[A](request: Request[A]) = {
    implicit val correlationContext = new CorrelationContext {
      override val correlationId = request.tags.getOrElse(CorrelatedLogging.CORRELATION_ID,
        throw new RuntimeException(CorrelatedLogging.CORRELATION_ID + " not set"))

      override val traceData = {
        def generateName: String = request.method + " " + request.uri

        val newId = TraceData.generateId
        ServerTraceData(
          traceId = request.headers.get(BraveHttpHeaders.TraceId.getName).fold(newId)(_.toLong),
          spanId = request.headers.get(BraveHttpHeaders.SpanId.getName).fold(newId)(_.toLong),
          parentSpanId = request.headers.get(BraveHttpHeaders.ParentSpanId.getName).map(_.toLong),
          shouldBeSampled = request.headers.get(BraveHttpHeaders.Sampled.getName).fold(true)(_.toBoolean),
          spanName = request.headers.get(BraveHttpHeaders.SpanName.getName).getOrElse(generateName)
        )
      }
    }

    correlationContext.traceData.serverReceived()

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
          val result = block(contextRequest).map {
            result =>
              Result(
                result.header,
                result.body.onDoneEnumerating {
                  contextRequest.traceData.serverSend()
                  contextRequest.traceData.toCollector(spanCollector)
                })
          }
          result.onFailure {
            case _ =>
              contextRequest.traceData.serverSend()
              contextRequest.traceData.toCollector(spanCollector)
          }

          result
      }
    }
  }

  object AuthenticatedAction extends ActionBuilder[ContextRequest] {
    override def invokeBlock[A](request: Request[A], block: (ContextRequest[A]) => Future[Result]) = {
      createContextRequest(request).flatMap {
        contextRequest =>
          val res = if (!contextRequest.isAuthenticated)
            onUnauthenticated(contextRequest)
          else
            block(contextRequest)

          val result = res.map {
            result =>
              Result(
                result.header,
                result.body.onDoneEnumerating {
                  contextRequest.traceData.serverSend()
                  contextRequest.traceData.toCollector(spanCollector)
                })
          }
          result.onFailure {
            case _ =>
              contextRequest.traceData.serverSend()
              contextRequest.traceData.toCollector(spanCollector)
          }

          result
      }
    }
  }

  def onUnauthenticated[A](request: ContextRequest[A]): Future[Result] = {
    Future.successful(Redirect(routes.UserLoginController.showForm()))
  }
}
