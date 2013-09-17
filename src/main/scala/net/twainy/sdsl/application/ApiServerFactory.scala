package net.twainy.sdsl.application

import com.twitter.finagle.http.service.RoutingService
import com.twitter.finagle.http.path.{Path, /, Root}
import com.twitter.finagle.builder.{ServerBuilder, Server}
import com.twitter.finagle.http._
import com.twitter.finagle.http.Method._
import java.net.InetSocketAddress
import com.twitter.finagle.redis.Client
import com.twitter.finagle.Service
import org.jboss.netty.handler.codec.http.HttpMethod
import com.twitter.finagle.http.path./
import com.twitter.finagle.http.RichHttp
import net.twainy.sdsl.application.filter.HandleExceptions
import net.twainy.sdsl.application.service.EchoService

class ApiServerFactory(redisClient: Client) {

  private def byPathObject[REQUEST](routes: PartialFunction[(HttpMethod, Path), Service[REQUEST, Response]]) =
    new RoutingService(
      new PartialFunction[Request, Service[REQUEST, Response]] {
        def apply(request: Request) = routes((request.method, Path(request.path)))

        def isDefinedAt(request: Request) = routes.isDefinedAt((request.method, Path(request.path)))
      })

  private val routingService = byPathObject {
    case (Get, Root / "echo") => HandleExceptions andThen new EchoService()(redisClient)
  }

  def build(name: String, portOpt: Option[Int]): Server = {
    val server = ServerBuilder()
      .codec(RichHttp[Request](Http()))
      .bindTo(new InetSocketAddress(portOpt.getOrElse(8080)))
      .name(name)
      .build(routingService)
    server
  }

}

