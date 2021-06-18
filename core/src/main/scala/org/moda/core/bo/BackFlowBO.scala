package org.moda.core.bo

import com.typesafe.scalalogging.Logger
import org.moda.common.database.DatabaseComponent
import org.moda.core.dao.{ChestnutTemplateDAO, ChestnutWorkFlowJsonDAO}
import org.moda.idl.BackFlowNodeType._
import org.moda.idl.{BackFlowNodeType, Bool, ChestnutTemplate, ChestnutWorkFlowJson, FlowManagerSaveReq, SimpleAuthUser}
import io.circe.generic.auto._
import io.circe.syntax._
import slick.dbio.{DBIOAction, Effect, NoStream}
import slick.sql.FixedSqlAction

import scala.concurrent.Future

object BackFlowBO {
  def apply()(implicit dc: DatabaseComponent): BackFlowBO = new BackFlowBO()
}

class BackFlowBO(implicit dc: DatabaseComponent) {

  import scala.concurrent.ExecutionContext.Implicits.global

  val logger: Logger = Logger(getClass)

  val templateDAO: ChestnutTemplateDAO = ChestnutTemplateDAO()
  val workFlowJsonDAO: ChestnutWorkFlowJsonDAO = ChestnutWorkFlowJsonDAO()

  def verifyFlowData(req: FlowManagerSaveReq): Boolean = {
    // 1. 做完整性校验 -- 必须有开始节点和结束节点
    // 且 只能有 一个 开始节点
    lazy val startEndCompleted = req.flowData.fold(false) { xx =>
      xx.nodes.count(xxn => str2BackFlowNodeType(xxn.`type`).isBackFlowNodeTypeStart) == 1 &&
      xx.nodes.exists(xxn => str2BackFlowNodeType(xxn.`type`).isBackFlowNodeTypeEnd)
    }

    // 2. 每个节点必须被作为 sourceNode 或者 targetNode
    // 不能有游离的节点
    lazy val edgeCompleted = req.flowData.fold(false) { xx =>
      xx.nodes.forall(xxn => xx.edges.exists { xxe =>
        xxn.id == xxe.sourceNodeId || xxn.id == xxe.targetNodeId
      })
    }

    // 3. 开始节点不能作为targetNode 结束节点不能作为sourceNode
    lazy val startEndLineComplete = req.flowData.fold(false) { xx =>
      val start = xx.nodes.find(xxn => str2BackFlowNodeType(xxn.`type`).isBackFlowNodeTypeStart)
      val ends = xx.nodes.filter(xxn => str2BackFlowNodeType(xxn.`type`).isBackFlowNodeTypeEnd)
      start.fold(false) { s => !xx.edges.exists(_.targetNodeId == s.id) } &&
      !ends.exists(e => xx.edges.exists(_.sourceNodeId == e.id))
    }
    // logger.info("x {},y {}, z {}", startEndCompleted, edgeCompleted, startEndLineComplete)
    startEndCompleted && edgeCompleted && startEndLineComplete
  }

  def str2BackFlowNodeType(str: String): BackFlowNodeType = {
    val ms: Map[String, BackFlowNodeType] = Map(
      "start"   ->  BACK_FLOW_NODE_TYPE_START,
      "rect"    ->  BACK_FLOW_NODE_TYPE_RECT,
      "switch"  ->  BACK_FLOW_NODE_TYPE_SWITCH,
      "end"     ->  BACK_FLOW_NODE_TYPE_END
    )

    ms.getOrElse(str, BACK_FLOW_NODE_TYPE_UNKNOWN)
  }

  /**
   * 保存模板
   * @param req
   * @param u
   * @return
   */
  def saveTemplate(req: FlowManagerSaveReq, u: SimpleAuthUser): FixedSqlAction[Long, NoStream, Effect.Write] = {
    templateDAO.insertTemplate(assembleTemplate(req, u))
  }

  def assembleTemplate(req: FlowManagerSaveReq, u: SimpleAuthUser): ChestnutTemplate =
    ChestnutTemplate(
      name = req.metaData.fold("")(_.flowName),
      isDeleted = Bool.False,
      createUser = u.id
    )

  /**
   * 保存json的格式数据
   * @param req
   */
  def saveFlowJson(req: FlowManagerSaveReq, templateId: Long): FixedSqlAction[Int, workFlowJsonDAO.dc.profile.api.NoStream, Effect.Write] = {
    workFlowJsonDAO.insertWorkFlowJsonTable(assembleFlowJson(req, templateId))
  }

  def assembleFlowJson(req: FlowManagerSaveReq, templateId: Long): ChestnutWorkFlowJson = ChestnutWorkFlowJson(
    templateId = templateId,
    flowVersion = req.metaData.fold("")(_.flowVersion),
    flowData = req.flowData.fold("{}")(_.asJson.toString)
  )

  def saveFlowData(req: FlowManagerSaveReq, u: SimpleAuthUser): Future[Boolean] = {
    Future {true}
  }
}