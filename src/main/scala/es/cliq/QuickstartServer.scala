package es.cliq

//#quick-start-server
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}
import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import es.cliq.services.WebApi

//#main-class
object QuickstartServer extends App with UserRoutes with WebApi {

  implicit val system: ActorSystem = ActorSystem("cliqes-http-server")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executor: ExecutionContext = system.dispatcher

  val userRegistryActor: ActorRef = system.actorOf(UserRegistryActor.props, "userRegistryActor")

  lazy val routes: Route = userRoutes

  val serverBinding: Future[Http.ServerBinding] = Http().bindAndHandle(routes, "localhost", 8080)

  serverBinding.onComplete {
    case Success(bound) =>
      println(s"Server online at http://${bound.localAddress.getHostString}:${bound.localAddress.getPort}/")
    case Failure(e) =>
      Console.err.println(s"Server could not start!")
      e.printStackTrace()
      system.terminate()
  }

  Await.result(system.whenTerminated, Duration.Inf)
}
