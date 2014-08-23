package de.leanovate.dose.cart.repository

import com.twitter.finagle.exp.Mysql
import com.twitter.finagle.Name
import java.net.InetSocketAddress
import com.twitter.finagle.exp.mysql._
import de.leanovate.dose.cart.cartconfig
import java.sql.Timestamp
import com.twitter.finagle.exp.mysql.StringValue
import scala.Some

object CartDB {
  val mysql =
    Mysql
      .withDatabase(cartconfig.dbName())
      .withCredentials(cartconfig.dbUsername(), cartconfig.dbPassword())
      .withCharset(Charset.Utf8_general_ci)
      .newRichClient(Name.bound(new InetSocketAddress(cartconfig.dbHost(), cartconfig.dbPort())), "local mysql")

  def stringValue(value: Value): Option[String] = value match {
    case StringValue(str) => Some(str)
    case _ => None
  }

  def intValue(value: Value): Option[Int] = value match {
    case ByteValue(i) => Some(i.toInt)
    case ShortValue(i) => Some(i.toInt)
    case IntValue(i) => Some(i)
    case LongValue(l) => Some(l.toInt)
    case BigDecimalValue(b) => Some(b.intValue())
    case _ =>
      None
  }

  def longValue(value: Value): Option[Long] = value match {
    case ByteValue(i) => Some(i.toLong)
    case ShortValue(i) => Some(i.toLong)
    case IntValue(i) => Some(i.toLong)
    case LongValue(l) => Some(l)
    case BigDecimalValue(b) => Some(b.longValue())
    case _ =>
      None
  }

  def timestampValue(value: Value): Option[Timestamp] = value match {
    case TimestampValue(timestamp) => Some(timestamp)
    case _ => None
  }
}
