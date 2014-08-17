package controllers

import scaldi.{Injectable, Injector}
import play.api.mvc.Controller
import backend.CustomerBackend

class ShopController(implicit inj: Injector) extends Controller with Injectable with Authentication {
  override val customerBackend = inject[CustomerBackend]

  def index = UnauthenticatedAction {
    implicit require =>
      Ok(views.html.shop.index())
  }
}
