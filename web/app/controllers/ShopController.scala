package controllers

import models.cart.CartItem
import models.user.Registration
import play.api.data.Form
import play.api.data.Forms._
import scaldi.{Injectable, Injector}
import play.api.mvc.Controller
import backend.{CartBackend, ProductBackend, CustomerBackend}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json

import scala.concurrent.Future

class ShopController(implicit inj: Injector) extends ContextAwareController {
  val productBackend = inject[ProductBackend]

  def index = UnauthenticatedAction.async {
    implicit request =>
      productBackend.getCategoryTree().map {
        categoryTree =>
          Ok(views.html.shop.index(categoryTree))
      }
  }

  def category(id: String) = UnauthenticatedAction.async {
    implicit request =>
      productBackend.getCategoryTree().zip(productBackend.getProductsForCategory(id)).map {
        case (categoryTree, products) =>
          Ok(views.html.shop.category(categoryTree, products.activeProducts))
      }
  }

  def product(id: String, option: Option[String]) = UnauthenticatedAction.async {
    implicit request =>
      productBackend.getProduct(id).map {
        product =>
          val selectedProduct = option.fold(product.options.headOption) {
            option =>
              product.options.find(_.name == option)
          }
          Ok(views.html.shop.productDetails(product, selectedProduct))
      }
  }

  def addToCart = UnauthenticatedAction.async {
    implicit request =>
      addToCartForm.bindFromRequest.fold(
        formWithErrors => {
          Future.successful(Ok(""))
        },
        toCart => {
          val cart = request.cart.map(Future.successful).getOrElse(cartBackend.createCart())

          cart.flatMap {
            cart =>
              val cartItem = CartItem(cart.id, None, toCart._1, toCart._2, toCart._3)
              cartBackend.addToCart(cartItem).map {
                createdItem =>
                  Redirect(routes.ShopController.showCart()).addingToSession("cartId" -> cart.id)
              }
          }
        }
      )
  }

  def showCart = UnauthenticatedAction.async {
    implicit request =>
      val cartItemsFuture = request.cart.fold(Future.successful(Seq.empty[CartItem])) {
        cart =>
          cartBackend.getCartItems(cart.id)
      }

      cartItemsFuture.map {
        cartItems =>
          Ok(views.html.shop.cartDetail(cartItems))
      }
  }

  val addToCartForm = Form(
    tuple(
      "productId" -> nonEmptyText,
      "option" -> nonEmptyText,
      "amount" -> number
    )
  )
}
