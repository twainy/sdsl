package net.twainy.sdsl.application

import org.apache.commons.daemon.{DaemonContext, Daemon}
import com.twitter.finagle.builder.Server
import com.twitter.finagle.Redis

/**
 * デーモンアプリケーション。
 */
class DaemonApplication extends Daemon with Application {

  private var config: ApplicationConfig = _

  protected val redisClient = Redis.newRichClient("localhost:4000")

  def init(context: DaemonContext) {
    DaemonApplication.daemonContext = context
    val args = context.getArguments
    config = loadConfigFromArgs(args)
  }

  def start() {
    DaemonApplication.server = Some(new ApiServerFactory(redisClient).build("daemon", config.apiPort))
  }

  def stop() {
    DaemonApplication.server.foreach(_.close())
  }

  def destroy() {

  }

}

/**
 * コンパニオンオブジェクト。
 */
object DaemonApplication {

  @volatile
  private var daemonContext: DaemonContext = _

  @volatile
  private var server: Option[Server] = None

  def getDaemonContext: DaemonContext = daemonContext

}
