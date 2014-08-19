package controllers

import play.api.mvc.Controller
import scaldi.{Injector, Injectable}
import play.api.data.Form
import play.api.data.Forms._
import models.user.Login
import backend.CustomerBackend
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._

class UserLoginController(implicit inj: Injector) extends Controller with Authentication with Injectable {
  override val customerBackend = inject[CustomerBackend]

  def showForm = UnauthenticatedAction {
    implicit request =>
      Ok(views.html.user.loginForm(loginForm))
  }

  def submitForm = UnauthenticatedAction.async {
    implicit request =>
      loginForm.bindFromRequest.fold(
        formWithErrors => {
          Future.successful(BadRequest(views.html.user.loginForm(formWithErrors)))
        },
        login => {
          customerBackend.login(login).map {
            result =>
              if (result.successful)
                Redirect(routes.ShopController.index()).withSession("customerId" -> result.customerId.get.toString)
              else
                BadRequest(views.html.user.loginForm(loginForm.withGlobalError("Login failed")))
          }
        }
      )
  }

  def logout = AuthenticatedAction {
    implicit request =>
      Redirect(routes.ApplicationController.index()).withNewSession
  }

  val loginForm = Form(
    mapping(
      "email" -> email,
      "password" -> text
    )(Login.apply)(Login.unapply).verifying()
  )
}
