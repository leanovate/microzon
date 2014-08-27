package de.leanovate.dose.cart.model

case class CartItems(
                      items: Seq[CartItem],
                      valid: Boolean,
                      totalCents: Int
                      )

object CartItems {
  def apply(items: Seq[CartItem]): CartItems = {
    CartItems(
      items = items,
      valid = items.forall(item => item.product.isDefined && item.priceInCent.isDefined),
      totalCents = items.foldLeft(0) {
        (total, item) => total + item.priceInCent.getOrElse(0)
      }
    )
  }
}