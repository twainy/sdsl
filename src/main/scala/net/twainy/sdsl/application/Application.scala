package net.twainy.sdsl.application

import com.twitter.util.Eval
import java.io.File

trait Application {

  private val DefaultConfigPath = "sdsl.conf"

  protected def loadConfigFromArgs(args: Array[String]): ApplicationConfig = {
    val configPath = args.toList match {
      case Nil => DefaultConfigPath
      case headArg :: _ => headArg
    }
    new Eval().apply[ApplicationConfig](new File(configPath))
  }

}
