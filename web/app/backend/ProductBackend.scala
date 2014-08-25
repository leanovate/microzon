package backend

import scaldi.{Injectable, Injector}
import logging.{CorrelatedWS, CorrelationContext, CorrelatedLogging}
import models.product.{ActiveProduct, Products, CategoryTree}
import play.api.libs.concurrent.Execution.Implicits._
import java.net.URLEncoder

class ProductBackend(implicit inj: Injector) extends Injectable with CorrelatedLogging {
  private val correlatedWS = inject[CorrelatedWS]
  private val baseUrl = inject[String](identified by "service.product.url")
  private val name = "Product"

  def getCategoryTree()(implicit collectionContext: CorrelationContext) = {
    correlatedWS.get(name, baseUrl + "/categories").map {
      response =>
        if (response.status != 200)
          throw new RuntimeException(s"get categories via product service failed status=${response.status}")
        response.json.as[CategoryTree]
    }
  }

  def getProductsForCategory(categoryId: String)(implicit collectionContext: CorrelationContext) = {
    correlatedWS.get(name, baseUrl + "/categories/" + URLEncoder.encode(categoryId, "UTF-8") + "/products").map {
      response =>
        if (response.status != 200)
          throw new RuntimeException(s"get products of category $categoryId via product service failed status=${response.status}")
        response.json.as[Products]
    }
  }

  def getProduct(productId: String)(implicit collectionContext: CorrelationContext) = {
    correlatedWS.get(name, baseUrl + "/products/" + URLEncoder.encode(productId, "UTF-8")).map {
      response =>
        if (response.status != 200)
          throw new RuntimeException(s"get product $productId via product service failed status=${response.status}")
        response.json.as[ActiveProduct]
    }
  }
}
