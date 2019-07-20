package es.cliq

import java.net.InetSocketAddress

import eventstore._
import eventstore.tcp._
import eventstore.j._

import _root_.akka.actor.Status.Failure
import _root_.akka.actor.{Actor, ActorLogging, ActorSystem, Props}

object WriteEventExample extends App {

  val system      = ActorSystem()

  val settings: Settings = new SettingsBuilder()
    .address(new InetSocketAddress("127.0.0.1", 1113))
    .defaultCredentials("admin", "changeit")
    .build
  val connection  = system.actorOf(ConnectionActor.props(settings))
  val event       = EventData.Json(eventType = "my-event",
    //eventId = randomUuid,
    eventId = java.util.UUID.fromString("e0897aec-199e-41bd-9e0c-b3040fcad261"),
    data = """{"a":"2"}""",
    metadata = """{"a":"não serve pra nada"}""")

  implicit val writeResult = system.actorOf(Props(WriteResult))

  connection ! WriteEvents(EventStream.Id("my-stream"), List(event))

  case object WriteResult extends Actor with ActorLogging {

    def receive = {
      case WriteEventsCompleted(range, position) =>
        log.info("range: {}, position: {}", range, position)
        context.system.terminate()

      case Failure(e: EsException) =>
        log.error(e.toString)
        context.system.terminate()
    }
  }
}
