package backend

import logging.{CorrelatedLogging, CorrelationContext}
import models.consul.{HealthInfo, NodeInfo}
import play.api.libs.concurrent.Execution.Implicits._
import scaldi.{Injectable, Injector}

import scala.concurrent.Future
import scala.util.Random

class ServiceFailover(implicit inj: Injector) extends Injectable with CorrelatedLogging {
  private val consulLookup = inject[ConsulLookup]
  private val random = new Random(System.currentTimeMillis())

  def retry[R](serviceName: String, uri: String)(requester: String => Future[R])(implicit collectionContext: CorrelationContext): Future[R] = {
    def tryRequest(healthInfos: Seq[HealthInfo], offset:Int, idx: Int): Future[R] = {
      val healthInfo = healthInfos((offset + idx) % healthInfos.size)
      val url = s"http://${healthInfo.Node.Address}:${healthInfo.Service.Port}$uri"

      withMdc {
        log.info(s"Service try: $url")
      }
      requester(url).recoverWith {
        case e: Throwable =>
          withMdc {
            log.info(s"Trigger retry on $e")
          }
          if (idx + 1 >= healthInfos.size) {
            Future.failed(e)
          } else {
            tryRequest(healthInfos, offset, idx + 1)
          }
      }
    }

    consulLookup.lookup(serviceName).flatMap {
      healthInfos =>
        if (healthInfos.isEmpty) {
          Future.failed(new RuntimeException(s"No active service nodes for service $serviceName"))
        } else {
          val offset = random.nextInt(healthInfos.size)

          tryRequest(healthInfos, offset, 0)
        }
    }
  }
}
