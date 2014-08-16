package controllers

import scaldi.Injector
import play.api.mvc.Controller

class ShopController(implicit inj: Injector) extends Controller with Authentication {
  def index = UnauthenticatedAction {
    implicit require =>
      Ok(views.html.shop.index())
  }
}
