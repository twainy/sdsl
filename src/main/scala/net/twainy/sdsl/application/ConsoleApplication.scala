package net.twainy.sdsl.application

import com.twitter.finagle.Redis


/**
 * コンソールアプリケーションを表すオブジェクト。
 *
 * 基本的にはデバッグに利用する。
 */
object ConsoleApplication extends App with Application {

  val config = loadConfigFromArgs(args)
  val redisClient = Redis.newRichClient(config.redisEndpoint.getOrElse("localhost:4000"))
  new ApiServerFactory(redisClient).build("console", config.apiPort)
}
