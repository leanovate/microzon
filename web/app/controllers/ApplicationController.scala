package controllers

import play.api.mvc._
import scaldi.{Injectable, Injector}
import backend.CustomerBackend

class ApplicationController(implicit inj: Injector) extends Controller with Injectable with Authentication {
  override val customerBackend = inject[CustomerBackend]

  def index = UnauthenticatedAction {
    implicit request =>
      Ok(views.html.index("Your new application is ready."))
  }

}