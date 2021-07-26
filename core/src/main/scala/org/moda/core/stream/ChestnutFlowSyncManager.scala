package org.moda.core.stream

import akka.actor.typed.ActorSystem
import akka.stream.UniqueKillSwitch
import org.moda.common.database.DatabaseComponent

import scala.concurrent.ExecutionContext

object ChestnutFlowSyncManager {
  def start()(implicit actorSystem: ActorSystem[_], ec: ExecutionContext, dc: DatabaseComponent): UniqueKillSwitch = {
    FlowAutoNodeExecuteStream().run()
  }
}
