package logging

trait CorrelationContext {
  def correlationId: String

  def traceData: ServerTraceData
}