package de.leanovate.dose.cart

import java.net.InetSocketAddress
import java.util.logging.LogManager

import com.twitter.finagle.Addr
import com.twitter.finagle.http.HttpServerTracingFilter
import com.twitter.finagle.tracing.DefaultTracer
import com.twitter.finagle.zipkin.thrift.ZipkinTracer
import com.twitter.finatra.FinatraServer
import com.twitter.util.Var
import de.leanovate.dose.cart.consul.ConsulLookup
import de.leanovate.dose.cart.logging.CorrelationHttpFilter
import de.leanovate.dose.cart.resources.CartResource
import org.flywaydb.core.Flyway
import org.slf4j.bridge.SLF4JBridgeHandler

object Application extends FinatraServer {
  val zipkinAddr = Var[Addr](Addr.Pending)

  addFilter(new HttpServerTracingFilter("cart-service"))
  addFilter(new CorrelationHttpFilter())

  register(new CartResource)

  override def main() {
    System.setProperty("PID", pid)
    LogManager.getLogManager.reset()
    SLF4JBridgeHandler.install()

    zipkinAddr.changes.respond {
      case Addr.Bound(nodes) =>
        val first = nodes.head.asInstanceOf[InetSocketAddress]
        log.info(s"Register zipkin tracer to $first")
        val zipkinTracer = ZipkinTracer.mk(first.getHostName, first.getPort)

        DefaultTracer.self = zipkinTracer
      case _ =>
    }
    ConsulLookup.lookup("zipkin-collector", zipkinAddr)


    migrateDatabase()

    super.main()
  }

  def migrateDatabase() {
    try {
      val flyway = new Flyway()

      flyway.setDataSource(cartconfig.jdbcUrl(), cartconfig.dbUsername(), cartconfig.dbPassword())

      flyway.migrate()
    } catch {
      case e: Throwable =>
        log.error(e, "Database migration failed")
    }
  }
}
