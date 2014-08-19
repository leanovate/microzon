package logging

trait CorrelationContext {
  def correlationId: String
}