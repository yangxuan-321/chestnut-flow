package org.moda.core.stream

import akka.actor.typed.ActorSystem
import akka.stream.UniqueKillSwitch

import scala.concurrent.ExecutionContext

object ChestnutFlowSyncManager {
  def start()(implicit actorSystem: ActorSystem[_], ec: ExecutionContext, dc: DatabaseComponent): UniqueKillSwitch = {
    BackstageMonitor().flow().run()
    ApiThrottleMonitor().flow().run()
    BasicDataSyncMonitor().flow().run()
    OAuthMonitor().flow().run()
    RefreshTokenMonitor().flow().run()
    StrategyExecMonitor().flow().run()
  }
}
