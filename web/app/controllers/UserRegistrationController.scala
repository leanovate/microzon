package controllers

import play.api.mvc.Controller
import play.api.data._
import play.api.data.Forms._
import models.user.Registration
import scaldi.{Injector, Injectable}
import backend.{CartBackend, CustomerBackend}
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._

class UserRegistrationController(implicit inj: Injector) extends ContextAwareController {

  def showForm = UnauthenticatedAction {
    implicit request =>
      Ok(views.html.user.registrationForm(registrationForm))
  }

  def submitForm = UnauthenticatedAction.async {
    implicit request =>
      registrationForm.bindFromRequest.fold(
        formWithErrors => {
          Future.successful(BadRequest(views.html.user.registrationForm(formWithErrors)))
        },
        registration => {
          customerBackend.registerCustomer(registration).map {
            result =>
              Redirect(routes.ShopController.index())
          }
        }
      )
  }

  val registrationForm = Form(
    mapping(
      "email" -> email,
      "password" -> nonEmptyText,
      "passwordRepeat" -> nonEmptyText
    )(Registration.apply)(Registration.unapply).
      verifying("Passwords do not match", registration => registration.password == registration.passwordRepeat)
  )
}
