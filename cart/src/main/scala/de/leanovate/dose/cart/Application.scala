package de.leanovate.dose.cart

import com.twitter.finatra.{config, FinatraServer}
import de.leanovate.dose.cart.resources.CartResource
import java.util.logging.LogManager
import org.slf4j.bridge.SLF4JBridgeHandler
import org.flywaydb.core.Flyway
import de.leanovate.dose.cart.repository.CartDB
import com.twitter.finagle.zipkin.thrift.ZipkinTracer
import com.twitter.finagle.tracing.{DefaultTracer, TracingFilter}
import com.twitter.finagle.netty3.Netty3Listener
import org.jboss.netty.handler.codec.http.{HttpRequest, HttpResponse}
import com.twitter.finagle.server.DefaultServer
import com.twitter.finagle.dispatch.SerialServerDispatcher
import com.twitter.finagle.http.HttpServerTracingFilter
import java.net.InetSocketAddress
import de.leanovate.dose.cart.logging.CorrelationHttpFilter

object Application extends FinatraServer {

  register(new CartResource)

  val zipkinTracer = ZipkinTracer.mk("192.168.254.20", 9410)

  DefaultTracer.self = zipkinTracer

  addFilter(new HttpServerTracingFilter("Cart"))
  addFilter(new CorrelationHttpFilter())

  override def main() {
    System.setProperty("PID", pid)
    LogManager.getLogManager.reset()
    SLF4JBridgeHandler.install()

    migrateDatabase()

    super.main()
  }

  def migrateDatabase() {
    val flyway = new Flyway()

    flyway.setDataSource(cartconfig.jdbcUrl(), cartconfig.dbUsername(), cartconfig.dbPassword())

    flyway.migrate()
  }
}
