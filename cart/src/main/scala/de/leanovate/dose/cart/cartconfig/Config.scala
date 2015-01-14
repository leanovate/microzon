package de.leanovate.dose.cart.cartconfig

import com.twitter.app.GlobalFlag
import com.twitter.finagle.Name

object dbName extends GlobalFlag[String]("cart", "Name of cart database")

object dbHost extends GlobalFlag[String]("localhost", "Database host")

object dbPort extends GlobalFlag[Int](3306, "Database port")

object dbUsername extends GlobalFlag[String]("cart", "Database username of the database")

object dbPassword extends GlobalFlag[String]("cart", "Database password of the database")

object jdbcUrl {
  def apply() = s"jdbc:mysql://${dbHost()}:${dbPort()}/${dbName()}"
}

object productHost extends GlobalFlag[String]("product-service=/$/de.leanovate.dose.cart.consul.service/product-service", "Host of the product service")

object consulHost extends GlobalFlag[String]("consul=127.0.0.1:8500", "Host of the consul agent")