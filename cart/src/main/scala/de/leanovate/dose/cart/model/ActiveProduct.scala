package de.leanovate.dose.cart.model


case class ActiveProduct(
                          id: String,
                          name: String,
                          description: Option[String],
                          options: Seq[ProductOption],
                          categories: Seq[String],
                          images: Seq[ImageRef]
                          )
