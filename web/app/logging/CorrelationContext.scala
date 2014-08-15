package logging

trait CorrelationContext {
  def sessionCorrelationId: String

  def requestCorrelationId: String
}