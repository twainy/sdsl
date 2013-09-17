package net.twainy.sdsl.application.service

import com.twitter.finagle.Service

trait ServiceSupport {
  this: Service[_, _] =>


}
