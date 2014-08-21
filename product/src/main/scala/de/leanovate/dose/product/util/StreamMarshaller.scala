package de.leanovate.dose.product.util

import akka.actor.{Actor, ActorRef, Props, ActorRefFactory}
import spray.httpx.marshalling.{MarshallingContext, Marshaller}
import scala.concurrent.ExecutionContext
import akka.util.{ByteString, Timeout}
import play.api.libs.iteratee._
import spray.http._
import scala.util.control.NonFatal
import spray.util.LoggingContext
import akka.io.Tcp
import spray.httpx.unmarshalling._

object StreamMarshaller {
  implicit def enumeratorMarshaller[T](implicit refFactory: ActorRefFactory,
                                       marshaller: Marshaller[T],
                                       ec: ExecutionContext,
                                       timeout: Timeout): Marshaller[Enumerator[T]] =
    Marshaller.apply[Enumerator[T]] {
      (enum, ctx) ⇒
        val chunker = refFactory.actorOf(Props(new Chunker(ctx, marshaller)))
        //pull the input through a buffer so that it starts being consumed/generated immediately
        //this allows to pull things through the pipeline so they're ready to be consumed before
        //the actual HTTP write starts happening.
        val res = enum &> Concurrent.buffer(50) |>>> chunkIteratee[T](chunker)

        res.onFailure {
          case NonFatal(e) =>
            chunker ! ChunkError(e)
        }
    }


  private def chunkIteratee[T](chunker: ActorRef)
                              (implicit ec: ExecutionContext,
                               refFactory: ActorRefFactory,
                               log: LoggingContext,
                               timeout: Timeout): Iteratee[T, Unit] = {

    import akka.pattern._


    def step(i: Input[T]): Iteratee[T, Unit] = {
      i match {
        case Input.EOF =>
          chunker ! ChunkingFinished
          Done((), Input.EOF)
        case Input.Empty => Cont[T, Unit](step)
        case Input.El(e) =>
          // tell the chunk actor to send the chunk, and wait for it to respond
          // before we send the next chunk
          val next = chunker.ask(Chunk(e)).map(cw => Cont[T, Unit](step))
          Iteratee.flatten(next)
      }
    }

    Cont[T, Unit](step)
  }

  private class Chunker[T](ctx: MarshallingContext, marshaller: Marshaller[T]) extends Actor {

    private case class SentOk(respondTo: ActorRef)

    private var connectionActor: ActorRef = _

    def receive: Receive = {
      case Chunk(chunk) =>
        import spray.httpx.marshalling.DelegatingMarshallingContext
        val respondTo = sender
        val chunkingCtx = new DelegatingMarshallingContext(ctx) {
          override def marshalTo(entity: HttpEntity, headers: HttpHeader*): Unit = {
            val data = entity.data
            val ack = SentOk(respondTo)
            if (connectionActor == null) {
              connectionActor = ctx.startChunkedMessage(HttpEntity(data), Some(ack))
            } else {
              connectionActor ! MessageChunk(data).withAck(ack)
            }
          }

          override def handleError(error: Throwable): Unit = {
            context.stop(self)
            ctx.handleError(error)
          }

          override def startChunkedMessage(entity: HttpEntity, sentAck: Option[Any], headers: Seq[HttpHeader])(implicit sender: ActorRef) =
            sys.error("Cannot marshal a enumerator of enumerators")
        }
        marshaller(chunk.asInstanceOf[T], chunkingCtx)

      case SentOk(respondTo) =>
        respondTo ! ChunkWritten

      case ChunkingFinished =>
        if (connectionActor == null) {
          //this means we haven't written any chunks - just send a simple empty non-chunked response
          ctx.marshalTo(HttpEntity.Empty)
        } else {
          connectionActor ! ChunkedMessageEnd
        }
        context.stop(self)

      case ChunkError(t) =>
        //if the response has yet to start streaming (i.e. we have an error before the first chunk has been generated)
        //this will send a non-chunked response
        //if we have started, not much we can do - the response will just end
        //either way, we signal the error.
        ctx.handleError(t)
        context.stop(self)

      case _: Tcp.ConnectionClosed ⇒
        context.stop(self)

    }
  }

  private case object ChunkingFinished

  private case class Chunk[T](chunk: T)

  private case object ChunkWritten

  private case class ChunkError(t: Throwable)

  case class RawEntity(data: ByteString, contentType: MediaType)

  implicit val entityUnmarshaller: Unmarshaller[RawEntity] =
    new Unmarshaller[RawEntity] {
      def apply(entity: HttpEntity) = entity match {
        case HttpEntity.NonEmpty(ct, data) => Right(RawEntity(data.toByteString, ct.mediaType))
        case _ => Left(ContentExpected)
      }
    }

}
