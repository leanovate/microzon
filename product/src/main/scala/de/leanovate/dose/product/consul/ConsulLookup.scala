package de.leanovate.dose.product.consul

import spray.client.pipelining._
import spray.http.HttpRequest
import spray.httpx.SprayJsonSupport._

import scala.concurrent.Future

object ConsulLookup {

  import de.leanovate.dose.product.Akka._

  val pipeline: HttpRequest => Future[Seq[ServiceNode]] =
    sendReceive ~> unmarshal[Seq[ServiceNode]]

  def lookup(serviceName: String): Future[Seq[ServiceNode]] = {
    val request = Get(s"http://localhost:8500/v1/catalog/service/$serviceName")

    pipeline(request)
  }
}
