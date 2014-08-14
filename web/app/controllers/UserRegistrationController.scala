package controllers

import play.api.mvc.Controller

object UserRegistrationController extends Controller with Correlated {

  def showForm = CorrelatedAction {
    Ok(views.html.user.registrationForm())
  }
}
