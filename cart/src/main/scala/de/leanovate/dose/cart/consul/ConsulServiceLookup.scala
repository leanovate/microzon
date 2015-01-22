package de.leanovate.dose.cart.consul

import java.net.{InetSocketAddress, URLEncoder}
import java.nio.charset.Charset

import com.twitter.finagle.{Addr, Service}
import com.twitter.logging.Logger
import com.twitter.util.{Duration, Time, Updatable}
import de.leanovate.dose.cart.util.Json
import org.jboss.netty.handler.codec.http._

class ConsulServiceLookup(service: Service[HttpRequest, HttpResponse], serviceName: String) {
  private val request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
    "/v1/catalog/service/" + URLEncoder.encode(serviceName, "UTF-8"))
  private val charset = Charset.forName("UTF-8")
  private val timeout = Duration.fromSeconds(5)
  private val log = Logger.get(getClass)
  @volatile
  private var lastLookup: (Time, Addr) = (Time.now - timeout * 2, Addr.Neg)

  def lookup(u: Updatable[Addr]) {
    val last = lastLookup
    if (last._1 + timeout < Time.now) {
      log.info(s"Looking up service $serviceName")
      service(request).onSuccess {
        response =>
          if (response.getStatus.getCode == 200) {
            val serviceNodes = Json.readArray(response.getContent.toString(charset), classOf[ServiceNode])
            log.info(s"Found nodes $serviceNodes")
            val addr = if (serviceNodes.isEmpty) {
              Addr.Neg
            } else {
              Addr.Bound(serviceNodes.map(serviceNode => new InetSocketAddress(serviceNode.Address, serviceNode.ServicePort)): _*)
            }
            lastLookup = (Time.now, addr)
            u() = addr
          } else {
            log.error(s"Consul service lookup failed: ${response.getStatus.getCode}")
            u() = last._2
          }
      }.onFailure {
        e =>
          log.error(e, "Consul service lookup failed")
          u() = Addr.Neg
      }
    } else {
      u() = lastLookup._2
    }
  }
}

