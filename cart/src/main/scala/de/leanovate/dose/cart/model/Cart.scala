package de.leanovate.dose.cart.model

import java.sql.Timestamp

case class Cart(id: Option[String],
                created: Option[Timestamp],
                itemCount: Option[Long],
                lastPosition: Option[Long])
