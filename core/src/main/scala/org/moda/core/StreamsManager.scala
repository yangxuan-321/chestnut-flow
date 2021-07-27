package org.moda.core

import akka.actor.typed.ActorSystem
import akka.stream.UniqueKillSwitch
import org.moda.common.database.DatabaseComponent
import org.moda.core.stream.ChestnutFlowSyncManager

/**
 * @author moda-master
 *2020/9/12 下午4:22
 */
object StreamsManager {

  def start()(implicit actorSystem: ActorSystem[_], dc: DatabaseComponent): UniqueKillSwitch = {
    ChestnutFlowSyncManager.start()
  }
}
