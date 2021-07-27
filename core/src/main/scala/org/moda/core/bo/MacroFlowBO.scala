package org.moda.core.bo

import java.util.Date

import com.typesafe.scalalogging.Logger
import org.moda.common.database.DatabaseComponent
import org.moda.core.dao._
import org.moda.idl.{ChestnutNode, _}

import scala.concurrent.Future

object MacroFlowBO {
  def apply()(implicit dc: DatabaseComponent): MacroFlowBO = new MacroFlowBO()
}

class MacroFlowBO(implicit dc: DatabaseComponent) {

  import DatabaseComponent.profile.api._

  import scala.concurrent.ExecutionContext.Implicits.global

  val logger: Logger = Logger(getClass)

  val workFlowInstanceDao: ChestnutWorkFlowInstanceDAO = ChestnutWorkFlowInstanceDAO()
  val nodeInstanceDAO: ChestnutNodeInstanceDAO = ChestnutNodeInstanceDAO()

  def saveWorkFlowStartData(req: WorkFlowServiceStartReq, u: SimpleAuthUser,
                            workFlow: ChestnutWorkFlow, node: ChestnutNode): Future[Either[String, Long]] = {
    val flowInstance = assembleWorkFlowInstance(req, u, workFlow)
    val nodeInstance = assembleNodeInstance(req, u, node)
    val action: DBIOAction[Long, NoStream, Effect.Write with Effect.Write] = for {
      // 插入流程实例表
      a <- workFlowInstanceDao.insertWorkFlowInstance(flowInstance)
      // 插入开始节点
      b <- nodeInstanceDAO.insertFlowNodeInstances(
        List(nodeInstance.copy(flowInstanceId = a))
      )
    } yield if (b.size == 1) a else 0L

    dc.db.run(action.transactionally).map { x =>
      if (x > 0) Right(x) else Left("保存失败")
    }
  }


  // 组装 流程实例
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

  // 组装 节点实例
  def assembleNodeInstance(req: WorkFlowServiceStartReq, u: SimpleAuthUser,
                               node: ChestnutNode): ChestnutNodeInstance = {
    ChestnutNodeInstance(
      nodeId = node.id,
      status = NodeInstanceStatus.NODE_INSTANCE_STATUS_NEW,
      paramValue = "{}",
      version = 0,
      createUser = u.id,
      createdAt = new Date().toInstant,
      updatedAt = new Date().toInstant
    )
  }

  def queryNewTaskInstanceInfo(nodeTypes: Vector[FlowNodeType]): Future[Vector[ChestnutNodeInstance]] = {
    nodeInstanceDAO.queryNewTaskInstanceInfo(nodeTypes)
  }

  def queryObtainTaskInstanceInfo(nodeTypes: Vector[FlowNodeType], ms: Long): Future[Vector[ChestnutNodeInstance]] = {
    nodeInstanceDAO.queryObtainTaskInstanceInfo(nodeTypes, ms)
  }
}