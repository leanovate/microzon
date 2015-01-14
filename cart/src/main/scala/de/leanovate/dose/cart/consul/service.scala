package de.leanovate.dose.cart.consul

import com.twitter.finagle._
import com.twitter.util._

class service extends Namer {
  override def lookup(path: Path): Activity[NameTree[Name]] = path match {
    case Path.Utf8(serviceName) =>
      val id = Path.Utf8("$", "de.leanovate.dose.cart.consul.service") ++ path
      ConsulLookup.lookupName(serviceName, id)
    case _ =>
      Activity.exception(new Exception("Invalid com.twitter.namer path " + path.show))
  }

  override def enum(prefix: Path): Activity[Dtab] = Activity.value(Dtab.empty)
}
