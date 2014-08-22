package de.leanovate.dose.cart.logging

import com.twitter.util.Local
import tyrex.services.UUID

object CorrelationContext {
  private[this] val local = new Local[String]

  def correlationId = local() match {
    case Some(id) => id
    case None =>
      val id = UUID.create()
      local.update(id)
      id
  }

  def setCorrelationId(id:String) = local.update(id)

  def clear() = local.clear()
}
