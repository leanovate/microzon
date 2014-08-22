package de.leanovate.dose.cart.repository

import tyrex.services.UUID
import com.twitter.finagle.exp.mysql._
import de.leanovate.dose.cart.model.Cart
import scala.Some

object CartRepository {

  import CartDB._

  val cartSelectById = mysql.prepare("select ID, CREATED, (select count(*) from CART_ITEM where CART_ID = ID) as itemCount, (select max(POSITION) from CART_ITEM where CART_ID = ID) as lastPosition from CART where ID = ? limit 1")
  val cartInsert = mysql.prepare("insert into CART (ID) values (?)")

  def findById(id: String) = {
    cartSelectById.select(id)(cartFromRow).map(_.headOption)
  }

  def insert(cart: Cart) = {
    val toInsert = cart.copy(id = Some(UUID.create()))

    cartInsert.apply(toInsert.id.get).map {
      case OK(1L, _, _, _, _) =>
        toInsert
      case result =>
        throw new RuntimeException(s"Failed to insert cart ${result.toString}")
    }
  }

  private def cartFromRow(row: Row) = {
    println(row.fields)
    println(row.values)
    Cart(
      id = row("ID").flatMap(stringValue),
      created = row("CREATED").flatMap(timestampValue),
      itemCount = row("itemCount").flatMap(longValue),
      lastPosition = row("lastPosition").flatMap(longValue))
  }

}
