package controllers

import scaldi.{Injectable, Injector}
import play.api.mvc.Controller
import backend.{ProductBackend, CustomerBackend}
import play.api.libs.concurrent.Execution.Implicits._

class ShopController(implicit inj: Injector) extends Controller with Injectable with Authentication {
  override val customerBackend = inject[CustomerBackend]

  val productBackend = inject[ProductBackend]

  def index = UnauthenticatedAction.async {
    implicit require =>
      productBackend.categoryTree().map {
        categoryTree =>
          Ok(views.html.shop.index(categoryTree))
      }
  }

  def category(id: String) = UnauthenticatedAction.async {
    implicit require =>
      productBackend.categoryTree().zip(productBackend.productsForCategory(id)).map {
        case (categoryTree, products) =>
          Ok(views.html.shop.category(categoryTree, products.activeProducts))
      }
  }

  def product(id:String) = UnauthenticatedAction {
    implicit require =>
      Ok("bla")
  }
}
