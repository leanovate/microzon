package controllers

import play.api._
import play.api.mvc._

object Application extends Controller with Correlated {

  def index = CorrelatedAction {
    Ok(views.html.index("Your new application is ready."))
  }

}