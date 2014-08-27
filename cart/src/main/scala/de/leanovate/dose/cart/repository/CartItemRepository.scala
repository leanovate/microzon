package de.leanovate.dose.cart.repository

import com.twitter.finagle.exp.mysql.{OK, Row}
import de.leanovate.dose.cart.model.{Cart, CartItem}

object CartItemRepository {

  import CartDB._

  val cartItemstForCart = mysql.prepare("select * from CART_ITEM where CART_ID = ? order by POSITION asc")
  val cartItemInsert = mysql.prepare("insert into CART_ITEM (CART_ID, POSITION, PRODUCT_ID, PRODUCT_OPTION, AMOUNT) values (?, ?, ?, ?, ?)")

  def findAllForCart(cartId: String) = {
    cartItemstForCart.select(cartId)(cartProductFromRow)
  }

  def insertToCart(cart: Cart, cartItem: CartItem) = {
    val toInsert = cartItem.copy(cartId = cart.id.get, position = Some(cart.lastPosition.fold(0L)(_ + 1L)))
    cartItemInsert.apply(toInsert.cartId, toInsert.position.get, toInsert.productId, toInsert.productOption, toInsert.amount).map {
      case OK(1L, _, _, _, _) =>
        toInsert
      case result =>
        throw new RuntimeException(s"Failed to insert cartItem ${result.toString}")

    }
  }

  private def cartProductFromRow(row: Row) = {
    CartItem(
      cartId = row("CART_ID").flatMap(stringValue).get,
      position = row("POSITION").flatMap(longValue),
      productId = row("PRODUCT_ID").flatMap(stringValue).get,
      productOption = row("PRODUCT_OPTION").flatMap(stringValue).get,
      amount = row("AMOUNT").flatMap(intValue).get,
      product = None,
      priceInCent = None
    )
  }
}
