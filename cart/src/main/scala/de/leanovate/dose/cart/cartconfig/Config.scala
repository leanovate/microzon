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

object productHost extends GlobalFlag[String]("Product=192.168.254.13:80", "Host of the product service")
