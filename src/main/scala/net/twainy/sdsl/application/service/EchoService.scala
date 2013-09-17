package net.twainy.sdsl.application.service


import com.twitter.finagle.{redis, Client, Service}
import com.twitter.util.Future
import com.twitter.finagle.stream.StreamResponse
import com.twitter.concurrent.{Broker, Offer}
import org.jboss.netty.buffer.ChannelBuffer
import org.jboss.netty.handler.codec.http.{HttpVersion, HttpResponseStatus, DefaultHttpResponse, HttpResponse}
import com.twitter.finagle.http.{Response, Request}
import java.lang.Iterable
import java.util
import java.util.Map.Entry

/**
 * Created with IntelliJ IDEA.
 * User: takeshi.wakasugi
 * Date: 13/08/09
 * Time: 16:20
 * To change this template use File | Settings | File Templates.
 */
class EchoService(redisClient: redis.Client) extends Service[Request, Response] with ServiceSupport {
  private val addBroker = new Broker[(String, Broker[ChannelBuffer])]
  private val remBroker = new Broker[(String, Broker[ChannelBuffer])]


  @scala.annotation.unspecialized
  def apply(request: Request): Future[Response] = Future {
    val requestProxy = Request(request)
    val userId = requestProxy.getParam("userId")
    val subscriber = new Broker[ChannelBuffer]
    addBroker !(userId, subscriber)

    new Response {
      def httpResponse: HttpResponse = new HttpResponse {
        def containsHeader(name: String): Boolean = ???

        def addHeader(name: String, value: scala.Any) {}

        def setStatus(status: HttpResponseStatus) {}

        def getHeaderNames: util.Set[String] = ???

        def removeHeader(name: String) {}

        def getContent: ChannelBuffer = ???

        def isKeepAlive: Boolean = ???

        def setContent(content: ChannelBuffer) {}

        def isChunked: Boolean = ???

        def getStatus: HttpResponseStatus = ???

        def setProtocolVersion(version: HttpVersion) {}

        def getHeaders: util.List[Entry[String, String]] = ???

        def getHeaders(name: String): util.List[String] = ???

        def clearHeaders() {}

        def setHeader(name: String, values: Iterable[_]) {}

        def setHeader(name: String, value: scala.Any) {}

        def setChunked(chunked: Boolean) {}

        def getContentLength(defaultValue: Long): Long = ???

        def getContentLength: Long = ???

        def getProtocolVersion: HttpVersion = ???

        def getHeader(name: String): String = ???
      }DefaultHttpResponse(request.getProtocolVersion(),HttpResponseStatus.BAD_GATEWAY)
    }
    /*
    new Response {
      def error: Offer[Throwable] = new Broker[Throwable].recv;

      def httpResponse: HttpResponse = new DefaultHttpResponse(request.getProtocolVersion,HttpResponseStatus.BAD_GATEWAY);

      def messages: Offer[ChannelBuffer] = subscriber.recv

      def release() {
        remBroker !(userId, subscriber)
        subscriber.recv foreach {
          _ => ()
        }
      }
    }
      */
  }
}
