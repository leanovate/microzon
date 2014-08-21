package de.leanovate.dose.product.logging


trait CorrelationContext {
  def correlationId: String
}
