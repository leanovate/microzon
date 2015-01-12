package controllers

import backend._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.Action
import scaldi.Injector

class StatusController(implicit inj: Injector) extends ContextAwareController {
  val consulLookup = inject[ConsulLookup]

  @volatile
  var healthy = true

  val serviceNames = Seq(
    BillingBackend.serviceName,
    CartBackend.serviceName,
    CustomerBackend.serviceName,
    ProductBackend.serviceName
  )

  def alive = Action {
    if (healthy) {
      NoContent
    } else {
      ServiceUnavailable
    }
  }

  def services = Action.async {
    implicit request =>
      consulLookup.lookupAll(serviceNames).map {
        serviceNodes =>
          Ok(views.html.status.services(serviceNodes))
      }
  }
}
