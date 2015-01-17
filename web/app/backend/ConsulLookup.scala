package backend

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import logging.CorrelatedLogging
import models.consul.HealthInfo
import play.api.libs.concurrent.Akka
import play.api.libs.concurrent.Execution.Implicits._
import scaldi.{Injectable, Injector}
import play.api.Play.current
import scala.collection.concurrent
import scala.concurrent.Future
import scala.concurrent.duration._

class ConsulLookup(implicit inj: Injector) extends Injectable with CorrelatedLogging {
  private val baseUrl = inject[String](identified by "consul.lookup.url")
  private val consulActors = concurrent.TrieMap.empty[String, ActorRef]
  private implicit val timeout = Timeout(5.seconds)

  def lookup(serviceName: String) = {
    def consulActor = consulActors.getOrElseUpdate(serviceName, Akka.system.actorOf(ConsulActor.props(baseUrl, serviceName)))

    (consulActor ? ConsulActor.Lookup).map {
      case ConsulActor.LookupResult(healthInfos) => healthInfos
      case result =>
        throw new RuntimeException("Unexpected result: " + result)
    }
  }

  def lookupAll(serviceNames: Seq[String]): Future[Seq[(String, Seq[HealthInfo])]] =
    Future.traverse(serviceNames) {
      serviceName =>
        lookup(serviceName).map {
          healthInfos =>
            serviceName -> healthInfos
        }
    }
}
