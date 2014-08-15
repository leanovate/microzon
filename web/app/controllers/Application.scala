package controllers

import play.api._
import play.api.mvc._

object Application extends Controller with Authentication {

  def index = UnauthenticatedAction {
    implicit request =>
      Ok(views.html.index("Your new application is ready."))
  }

}