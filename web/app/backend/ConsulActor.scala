package backend

import java.net.URLEncoder

import akka.actor.{ActorLogging, ActorRef, FSM, Props}
import backend.ConsulActor.{Lookup, _}
import models.consul.HealthInfo
import play.api.Logger
import play.api.libs.json.{JsSuccess, Json}
import play.api.libs.ws.{WS, WSResponse}
import scala.concurrent.duration._
import play.api.Play.current
import scala.util.{Failure, Success, Try}

class ConsulActor(baseUrl: String, serviceName: String) extends FSM[State, Data] with ActorLogging {
  val url = baseUrl + URLEncoder.encode(serviceName, "UTF-8") + "?passing"

  import context.dispatcher
  import context.system

  startWith(Initial, NoData)

  when(Initial) {
    case Event(Lookup, _) =>
      lookup()
      goto(InitialLookup) using WaitingReceivers(Seq(sender()))
  }

  when(InitialLookup) {
    case Event(Lookup, WaitingReceivers(receivers)) =>
      stay() using WaitingReceivers(receivers :+ sender())
    case Event(result: ConsulResult, WaitingReceivers(receivers)) =>
      receivers.foreach(_ ! LookupResult(result.healthInfos))
      result.lookupIndex match {
        case Some(lookupIndex) =>
          blockingLookup(lookupIndex)
        case None =>
          system.scheduler.scheduleOnce(5.seconds)(lookup())
      }
      goto(Polling) using CachedData(result.healthInfos)
    case Event(ConsulFailed, WaitingReceivers(receivers)) =>
      receivers.foreach(_ ! LookupResult(Seq.empty))
      goto(Initial) using NoData
  }

  when(Polling) {
    case Event(Lookup, CachedData(healthInfos)) =>
      sender() ! LookupResult(healthInfos)
      stay()
    case Event(result: ConsulResult, _) =>
      result.lookupIndex match {
        case Some(lookupIndex) =>
          blockingLookup(lookupIndex)
        case None =>
          system.scheduler.scheduleOnce(5.seconds)(lookup())
      }
      stay() using CachedData(result.healthInfos)
    case Event(ConsulFailed, _) =>
      system.scheduler.scheduleOnce(5.seconds)(lookup())
      stay()
  }

  private def lookup() {
    log.info("Perform consul lookup: " + url)
    WS.url(url).get().onComplete(handleResponse)
  }

  private def blockingLookup(lookupIndex: String) {
    log.info("Perform blocking consul lookup: " + url)
    WS.url(url + "&wait=40s&index=" + URLEncoder.encode(lookupIndex, "UTF-8")).get().onComplete(handleResponse)
  }

  private def handleResponse(result: Try[WSResponse]) = result match {
    case Success(response) if response.status == 200 =>
      Json.fromJson[Seq[HealthInfo]](Json.parse(response.body)) match {
        case JsSuccess(healthInfos, _) =>
          self ! ConsulResult(healthInfos, response.header("X-Consul-Index"))
        case error =>
          Logger.error(s"Invalid json response from consul: $error")
          self ! ConsulFailed
      }
    case Success(response) =>
      log.error(s"Consul lookup failed with status ${response.status}")
      self ! ConsulFailed
    case Failure(e) =>
      log.error(e, "Consul lookup failed")
      self ! ConsulFailed
  }
}

object ConsulActor {
  def props(baseUrl: String, serviceName: String) = Props(classOf[ConsulActor], baseUrl, serviceName)

  sealed trait State

  case object Initial extends State

  case object InitialLookup extends State

  case object Polling extends State

  sealed trait Data

  case object NoData extends Data

  case class WaitingReceivers(receivers: Seq[ActorRef]) extends Data

  case class CachedData(healthInfos: Seq[HealthInfo]) extends Data

  sealed trait Msg

  case object Lookup extends Msg

  case class LookupResult(healthInfos: Seq[HealthInfo]) extends Msg

  case object ConsulFailed extends Msg

  case class ConsulResult(healthInfos: Seq[HealthInfo], lookupIndex: Option[String]) extends Msg

}