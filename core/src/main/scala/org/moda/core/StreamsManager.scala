package org.moda.core

import akka.actor.typed.ActorSystem
import akka.stream.UniqueKillSwitch
import com.zz.cdp.monitor.streams.{ApiThrottleMonitor, BackstageMonitor, BasicDataSyncMonitor, OAuthMonitor, RefreshTokenMonitor, StrategyExecMonitor}
import org.moda.core.database.DatabaseComponent

import scala.concurrent.ExecutionContext

/**
 * @author moda-master
 *2020/9/12 下午4:22
 */
object StreamsManager {

  def start()(implicit actorSystem: ActorSystem[_], ec: ExecutionContext, dc: DatabaseComponent): UniqueKillSwitch = {
//    DataSyncStream().flow().run()
  }
}
