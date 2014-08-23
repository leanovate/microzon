package models

import models.user.Customer
import models.cart.Cart

trait UserContext {
  def isAuthenticated: Boolean

  def customer: Option[Customer]

  def cart: Option[Cart]
}
