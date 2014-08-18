package de.leanovate.dose.product

import akka.actor.ActorSystem

object Akka {
  implicit val actorSystem = ActorSystem("actorsystem")

  implicit val executor = Akka.actorSystem.dispatcher
}
