package models

import models.user.Customer

trait UserContext {
  def isAuthenticated: Boolean

  def customer: Option[Customer]
}
