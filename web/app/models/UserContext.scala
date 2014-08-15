package models

trait UserContext {
  def isAuthenticated: Boolean

  def customerId: Option[Long]
}
