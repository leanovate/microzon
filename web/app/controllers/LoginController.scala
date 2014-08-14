package controllers

import play.api.mvc.Controller

object LoginController extends Controller with Correlated {
  def showForm = CorrelatedAction {
    Ok(views.html.user.loginForm())
  }
}
