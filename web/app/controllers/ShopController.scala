package controllers

import scaldi.{Injectable, Injector}
import play.api.mvc.Controller
import backend.{ProductBackend, CustomerBackend}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json

class ShopController(implicit inj: Injector) extends Controller with Injectable with Authentication {
  override val customerBackend = inject[CustomerBackend]

  val productBackend = inject[ProductBackend]

  def index = UnauthenticatedAction.async {
    implicit require =>
      productBackend.getCategoryTree().map {
        categoryTree =>
          Ok(views.html.shop.index(categoryTree))
      }
  }

  def category(id: String) = UnauthenticatedAction.async {
    implicit require =>
      productBackend.getCategoryTree().zip(productBackend.getProductsForCategory(id)).map {
        case (categoryTree, products) =>
          Ok(views.html.shop.category(categoryTree, products.activeProducts))
      }
  }

  def product(id: String, option: Option[String]) = UnauthenticatedAction.async {
    implicit require =>
      productBackend.getProduct(id).map {
        product =>
          val selectedProduct = option.fold(product.options.headOption) {
            option =>
              product.options.find(_.name == option)
          }
          Ok(views.html.shop.productDetails(product, selectedProduct))
      }
  }
}
