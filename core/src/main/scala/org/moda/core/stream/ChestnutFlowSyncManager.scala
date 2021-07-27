package org.moda.core.stream

import akka.actor.typed.ActorSystem
import akka.stream.UniqueKillSwitch
import org.moda.common.database.DatabaseComponent

object ChestnutFlowSyncManager {
  def start()(implicit actorSystem: ActorSystem[_], dc: DatabaseComponent): UniqueKillSwitch = {
//    FlowAutoNodeExecuteStream().run()
    FlowAutoNodeTimeOutStream().run()
  }
}
