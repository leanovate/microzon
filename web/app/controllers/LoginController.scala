package controllers

import play.api.mvc.Controller
import scaldi.{Injector, Injectable}
import play.api.data.Form
import play.api.data.Forms._
import models.user.Login
import backend.CustomerBackend
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._

class LoginController(implicit inj: Injector) extends Controller with Authentication with Injectable {
  private val customerBackend = inject[CustomerBackend]

  def showForm = UnauthenticatedAction {
    implicit request =>
      Ok(views.html.user.loginForm(loginForm))
  }

  def submitForm = UnauthenticatedAction.async {
    implicit require =>
      loginForm.bindFromRequest.fold(
        formWithErrors => {
          Future.successful(BadRequest(views.html.user.loginForm(formWithErrors)))
        },
        login => {
          customerBackend.login(login).map {
            result =>
              Ok(result.toString)
          }
        }
      )
  }

  val loginForm = Form(
    mapping(
      "email" -> email,
      "password" -> text
    )(Login.apply)(Login.unapply).verifying()
  )
}
