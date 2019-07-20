package es.cliq

import java.net.InetSocketAddress

import eventstore._
import eventstore.tcp._

import _root_.akka.actor.Status.Failure
import _root_.akka.actor.{Actor, ActorLogging, ActorSystem, Props}

object ReadEventExample extends App {
  val system = ActorSystem()

  val settings = Settings(
    address = new InetSocketAddress("127.0.0.1", 1113),
    defaultCredentials = Some(UserCredentials("admin", "changeit"))
  )

  val connection = system.actorOf(ConnectionActor.props(settings))
  implicit val readResult = system.actorOf(Props[ReadResult])

  connection ! ReadEvent(EventStream.Id("my-stream"), EventNumber.Last, resolveLinkTos = true)

  class ReadResult extends Actor with ActorLogging {
    def receive = {
      case ReadEventCompleted(event) =>
        log.info("event: {}", event)
        context.system.terminate()

      case Failure(e: EsException) =>
        log.error(e.toString)
        context.system.terminate()
    }
  }
}
