package de.leanovate.dose.cart.connectors

import com.twitter.finagle.Http
import java.net.URLEncoder
import de.leanovate.dose.cart.cartconfig
import org.jboss.netty.handler.codec.http.{HttpMethod, HttpVersion, DefaultHttpRequest}
import java.nio.charset.Charset
import de.leanovate.dose.cart.model.ActiveProduct
import de.leanovate.dose.cart.util.Json
import de.leanovate.dose.cart.logging.CorrelationContext

object ProductConnector {
  val charset = Charset.forName("UTF-8")

  def getProduct(productId: String) = {

    val client = Http.newService(cartconfig.productHost())
    val request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
      "/products/" + URLEncoder.encode(productId, "UTF-8"))
    request.headers().add("User-Agent", "Finagle/Netty")
    request.headers().add("Accept", "application/json")
    request.headers().add("Host", cartconfig.productHost())
    request.headers().add("X-CorrelationId", CorrelationContext.correlationId)

    client(request).map {
      response =>
        if (response.getStatus.getCode == 200) {
          Some(Json.readValue(response.getContent.toString(charset), classOf[ActiveProduct]))
        } else {
          None
        }
    }
  }
}
