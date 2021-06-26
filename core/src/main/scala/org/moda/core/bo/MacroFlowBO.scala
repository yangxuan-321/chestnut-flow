package org.moda.core.bo

import java.util.Date

import com.typesafe.scalalogging.Logger
import io.circe.generic.auto._
import io.circe.syntax._
import org.moda.common.database.DatabaseComponent
import org.moda.common.util.UUID
import org.moda.core.dao._
import org.moda.idl.FlowNodeType._
import org.moda.idl.{ChestnutNode, _}
import slick.dbio.{DBIOAction, Effect, NoStream}
import slick.sql.FixedSqlAction

object MacroFlowBO {
  def apply()(implicit dc: DatabaseComponent): MacroFlowBO = new MacroFlowBO()
}

class MacroFlowBO(implicit dc: DatabaseComponent) {
  import scala.concurrent.ExecutionContext.Implicits.global

  val logger: Logger = Logger(getClass)

  val workFlowDAO: ChestnutWorkFlowDAO = ChestnutWorkFlowDAO()
  val nodeDAO: ChestnutNodeDAO = ChestnutNodeDAO()
  val nodeRouterDAO: ChestnutNodeRouterDAO = ChestnutNodeRouterDAO()

  def saveWorkFlowStartData(req: WorkFlowServiceStartReq, u: SimpleAuthUser,
                            workFlow: ChestnutWorkFlow, node: ChestnutNode): F_[Option[A_]] = {
    for {
      a <- 
    }
  }

  def assembleWorkFlowInstance(req: WorkFlowServiceStartReq, u: SimpleAuthUser,
                               workFlow: ChestnutWorkFlow): ChestnutWorkFlowInstance = {
    ChestnutWorkFlowInstance(
      flowId = workFlow.id,
      status = FlowInstanceStatus.FLOW_INSTANCE_STATUS_NEW,
      createUser = u.id,
      createdAt = new Date().toInstant,
      updatedAt = new Date().toInstant
    )
  }
}