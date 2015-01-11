package backend

import models.consul.ServiceNode
import scaldi.{Injectable, Injector}
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.Future
import scala.util.Random

class ServiceFailover(implicit inj: Injector) extends Injectable {
  private val consulLookup = inject[ConsulLookup]
  private val random = new Random(System.currentTimeMillis())

  def retry[R](serviceName: String, uri: String)(requester: String => Future[R]): Future[R] = {
    def tryRequest(serviceNodes: Seq[ServiceNode], idx: Int): Future[R] = {
      val offset = random.nextInt(serviceNodes.size)
      val node = serviceNodes((offset + idx) % serviceNodes.size)
      val url = s"http://${node.Address}:${node.ServicePort}$uri"

      requester(url).recoverWith {
        case e: Throwable =>
          if (idx + 1 >= serviceNodes.size) {
            Future.failed(e)
          } else {
            tryRequest(serviceNodes, idx + 1)
          }
      }
    }

    consulLookup.lookup(serviceName).flatMap {
      serviceNodes =>
        if (serviceNodes.isEmpty) {
          Future.failed(new RuntimeException(s"No active service nodes for service $serviceName"))
        } else {
          tryRequest(serviceNodes, 0)
        }
    }
  }
}
