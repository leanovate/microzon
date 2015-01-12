package de.leanovate.dose.cart.consul

import java.net.URLEncoder
import java.nio.charset.Charset

import com.twitter.finagle.HttpClient
import com.twitter.logging.Logger
import com.twitter.util.Future
import de.leanovate.dose.cart.cartconfig
import de.leanovate.dose.cart.util.Json
import org.jboss.netty.handler.codec.http.{DefaultHttpRequest, HttpMethod, HttpVersion}

object ConsulConnector {
  val service = HttpClient.newClient(cartconfig.consulHost()).toService
  val charset = Charset.forName("UTF-8")
  val log = Logger.get(getClass)

  def lookup(serviceName: String): Future[Seq[ServiceNode]] = {
    val request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
      "/v1/catalog/service/" + URLEncoder.encode(serviceName, "UTF-8"))

    service.apply(request).map {
      response =>
        if (response.getStatus.getCode == 200) {
          Json.readValue(response.getContent.toString(charset), classOf[Seq[ServiceNode]])
        } else {
          log.error("Consul service lookup failed: " + response.getStatus.getCode)
          Seq.empty
        }
    }
  }
}
