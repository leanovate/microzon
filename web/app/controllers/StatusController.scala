package controllers

import backend._
import play.api.libs.concurrent.Execution.Implicits._
import scaldi.Injector

class StatusController(implicit inj: Injector) extends ContextAwareController {
  val consulLookup = inject[ConsulLookup]

  val serviceNames = Seq(
    BillingBackend.serviceName,
    CartBackend.serviceName,
    CustomerBackend.serviceName,
    ProductBackend.serviceName
  )

  def services = UnauthenticatedAction.async {
    implicit request =>
      consulLookup.lookupAll(serviceNames).map {
        serviceNodes =>
          Ok(views.html.status.services(serviceNodes))
      }
  }
}
