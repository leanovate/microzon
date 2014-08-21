package backend

import scaldi.{Injectable, Injector}
import logging.{CorrelatedWS, CorrelationContext, CorrelatedLogging}
import models.product.{Products, CategoryTree}
import play.api.libs.concurrent.Execution.Implicits._
import java.net.URLEncoder

class ProductBackend(implicit inj: Injector) extends Injectable with CorrelatedLogging {
  private val baseUrl = inject[String](identified by "service.product.url")

  def categoryTree()(implicit collectionContext: CorrelationContext) = {
    CorrelatedWS.url(baseUrl + "/categories").get().map {
      response =>
        if (response.status != 200)
          throw new RuntimeException(s"get categories via product service failed status=${response.status}")
        response.json.as[CategoryTree]
    }
  }

  def productsForCategory(categoryId: String)(implicit collectionContext: CorrelationContext) = {
    CorrelatedWS.url(baseUrl + "/categories/" + URLEncoder.encode(categoryId, "UTF-8") + "/products").get().map {
      response =>
        if (response.status != 200)
          throw new RuntimeException(s"get products of category $categoryId via product service failed status=${response.status}")
        response.json.as[Products]

    }
  }
}
