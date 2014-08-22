package de.leanovate.dose.cart.logging

import ch.qos.logback.classic.pattern.ClassicConverter
import ch.qos.logback.classic.spi.ILoggingEvent

class CorrelationConverter extends ClassicConverter {
  override def convert(event: ILoggingEvent) = CorrelationContext.correlationId
}
