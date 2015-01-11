package backend

import java.net.URLEncoder

import logging.{CorrelatedLogging, CorrelatedWS, CorrelationContext}
import models.product.{ActiveProduct, CategoryTree, Products}
import play.api.libs.concurrent.Execution.Implicits._
import scaldi.{Injectable, Injector}

class ProductBackend(implicit inj: Injector) extends Injectable with CorrelatedLogging {
  private val correlatedWS = inject[CorrelatedWS]
  private val serviceFailover = inject[ServiceFailover]

  import backend.ProductBackend._

  def getCategoryTree()(implicit collectionContext: CorrelationContext) =
    serviceFailover.retry(serviceName, "/categories") {
      url =>
        correlatedWS.get(serviceName, url).map {
          response =>
            if (response.status != 200)
              throw new RuntimeException(s"get categories via product service failed status=${response.status}")
            response.json.as[CategoryTree]
        }
    }

  def getProductsForCategory(categoryId: String)(implicit collectionContext: CorrelationContext) =
    serviceFailover.retry(serviceName, "/categories/" + URLEncoder.encode(categoryId, "UTF-8") + "/products") {
      url =>
        correlatedWS.get(serviceName, url).map {
          response =>
            if (response.status != 200)
              throw new RuntimeException(s"get products of category $categoryId via product service failed status=${response.status}")
            response.json.as[Products]
        }
    }

  def getProduct(productId: String)(implicit collectionContext: CorrelationContext) =
    serviceFailover.retry(serviceName, "/products/" + URLEncoder.encode(productId, "UTF-8")) {
      url =>
        correlatedWS.get(serviceName, url).map {
          response =>
            if (response.status != 200)
              throw new RuntimeException(s"get product $productId via product service failed status=${response.status}")
            response.json.as[ActiveProduct]
        }
    }
}

object ProductBackend {
  val serviceName = "product-service"
}