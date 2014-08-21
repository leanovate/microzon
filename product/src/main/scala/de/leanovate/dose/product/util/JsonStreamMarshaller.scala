import akka.actor._
import akka.io.Tcp
import play.api.libs.iteratee._
import scala.concurrent.ExecutionContext
import spray.http._
import spray.httpx.marshalling.{MarshallingContext, Marshaller}
import akka.util.{ByteString, Timeout}
import scala.util.control.NonFatal
import spray.util.LoggingContext
import spray.httpx.unmarshalling._

object JsonStreamMarshaller {

  /**
   * Create a marshaller that can stream a sequence of objects from a Play Enumerator. This allows non-blocking stream
   * processing of JSON responses of arbitrary length.
   */
  implicit def enumeratorMarshaller[T](implicit refFactory: ActorRefFactory,
                                       marshaller: Marshaller[T],
                                       ec: ExecutionContext,
                                       timeout: Timeout): Marshaller[Enumerator[T]] =
    Marshaller.of[Enumerator[T]](MediaTypes.`application/json`) {
      (enum, contentType, ctx) ⇒
        val chunker = refFactory.actorOf(Props(new JsonChunker(ctx, contentType, marshaller)))
        //pull the input through a buffer so that it starts being consumed/generated immediately
        //this allows to pull things through the pipeline so they're ready to be consumed before
        //the actual HTTP write starts happening.
        val res = enum &> Concurrent.buffer(50) |>>> jsonChunkIteratee[T](chunker)

        res.onFailure {
          case NonFatal(e) =>
            chunker ! ChunkError(e)
        }
    }


  private def jsonChunkIteratee[T](chunker: ActorRef)
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

  private class JsonChunker[T](ctx: MarshallingContext, contentType: ContentType, marshaller: Marshaller[T]) extends Actor {

    private case class SentOk(respondTo: ActorRef)

    private val startData = HttpData("[")
    private val endData = HttpData("]")
    private val comma = HttpData(",")
    private var connectionActor: ActorRef = _

    def receive: Receive = {
      case Chunk(chunk) =>
        import spray.httpx.marshalling.DelegatingMarshallingContext
        val respondTo = sender
        val chunkingCtx = new DelegatingMarshallingContext(ctx) {
          override def marshalTo(entity: HttpEntity, headers: HttpHeader*): Unit = {
            var data = entity.data
            //insert a comma before each value if we've written more than one
            if (connectionActor == null) {
              connectionActor = ctx.startChunkedMessage(HttpEntity(contentType, startData))
            } else {
              //every chunk except the first gets a comma prepended
              data = comma +: data
            }
            val ack = SentOk(respondTo)
            connectionActor ! MessageChunk(data).withAck(ack)
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
          ctx.marshalTo(HttpEntity(contentType, startData +: endData))
        } else {
          connectionActor ! MessageChunk(endData)
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