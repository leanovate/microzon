package de.leanovate.dose.cart.model

case class CartItem(cartId: String,
                    position: Option[Long],
                    productId: String,
                    productOption: String,
                    amount: Int,
                    product: Option[ActiveProduct],
                    priceInCent: Option[Int]) {
  def fillProduct(_product: ActiveProduct) = {
    copy(
      product = Some(_product),
      priceInCent = _product.options.find(_.name == productOption).map(_.priceInCent * amount))
  }
}

