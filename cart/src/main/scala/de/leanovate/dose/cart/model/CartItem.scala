package de.leanovate.dose.cart.model

case class CartItem(cartId: String, position: Option[Long], productId: String, productOption: String)

