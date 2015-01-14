package de.leanovate.dose.cart.consul

import com.twitter.finagle.{Addr, HttpClient, Name, NameTree}
import com.twitter.util.{Var, Activity, Updatable}
import de.leanovate.dose.cart.cartconfig

import scala.collection.concurrent

object ConsulLookup {
  private val service = HttpClient.newClient(cartconfig.consulHost()).toService
  private val serviceLookups = concurrent.TrieMap.empty[String, ConsulServiceLookup]

  def lookup(serviceName: String, u: Updatable[Addr]) =
    serviceLookups.getOrElseUpdate(serviceName, new ConsulServiceLookup(service, serviceName)).lookup(u)

  def lookupName(serviceName: String, id: Any) = {
    val va = Var[Addr](Addr.Pending)
    val name = Name.Bound(va, id)

    lookup(serviceName, va)

    Activity(va map {
      case Addr.Bound(_) => Activity.Ok(NameTree.Leaf(name))
      case Addr.Neg => Activity.Ok(NameTree.Neg)
      case Addr.Pending => Activity.Pending
      case Addr.Failed(exc) => Activity.Failed(exc)
    })
  }
}
