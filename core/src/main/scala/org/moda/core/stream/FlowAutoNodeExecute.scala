package com.zz.cdp.monitor.streams

import akka.stream.scaladsl.{RestartSource, Source}
import org.moda.common.database.DatabaseComponent
import com.typesafe.scalalogging.Logger
import scala.concurrent.duration._

/**
 * 将自动节点自动执行流
 * TODO 没做完，有空做
 */
object FlowAutoNodeExecute {
  def apply()(implicit dc: DatabaseComponent): FlowAutoNodeExecute = new FlowAutoNodeExecute()
}

class FlowAutoNodeExecute {

  private [this] val logger = Logger(getClass)


  def run(): Unit = {
    RestartSource.withBackoff(minBackoff = 1.seconds, maxBackoff = 60.seconds, randomFactor = 0.2) { () =>
      Source.future {
        Source.tick(1.second, 100.millis, ())
          .mapAsync(1) {

          }
      }
    }
  }

  def queryAutoTaskNodeAllInfo(): Unit = {
    service.queryAutoTaskInfo()
  }
}
