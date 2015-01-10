package controllers

import backend.ConsulLookup
import play.api.libs.json.Json
import scaldi.Injector
import play.api.libs.concurrent.Execution.Implicits._

class StatusController(implicit inj:Injector) extends ContextAwareController {
  val consulLookup = inject[ConsulLookup]

  def services = UnauthenticatedAction.async {
    implicit request =>
      consulLookup.lookup("customer-service").map {
        serviceNodes =>
          Ok(Json.toJson(serviceNodes))
      }
  }
}
