package net.twainy.sdsl.application

/**
 * 設定ファイルを表すトレイト。
 */
trait ApplicationConfig {

  /** API用ポート番号 */
  val apiPort: Option[Int]

  /** ストリーミング用ポート番号 */
  val streamingPort: Option[Int]

  /** Redis接続先 */
  val redisEndpoint: Option[String]
}


