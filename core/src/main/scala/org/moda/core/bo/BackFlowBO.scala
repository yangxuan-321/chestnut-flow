package org.moda.core.bo

import java.util.Date

import com.typesafe.scalalogging.Logger
import io.circe.generic.auto._
import io.circe.syntax._
import org.moda.common.database.DatabaseComponent
import org.moda.core.dao._
import org.moda.idl.FlowNodeType._
import org.moda.idl._
import slick.dbio.{DBIOAction, Effect, NoStream}
import slick.sql.FixedSqlAction

object BackFlowBO {
  def apply()(implicit dc: DatabaseComponent): BackFlowBO = new BackFlowBO()
}

class BackFlowBO(implicit dc: DatabaseComponent) {

  import scala.concurrent.ExecutionContext.Implicits.global

  val logger: Logger = Logger(getClass)

  val templateDAO: ChestnutTemplateDAO = ChestnutTemplateDAO()
  val workFlowJsonDAO: ChestnutWorkFlowJsonDAO = ChestnutWorkFlowJsonDAO()
  val workFlowDAO: ChestnutWorkFlowDAO = ChestnutWorkFlowDAO()
  val nodeDAO: ChestnutNodeDAO = ChestnutNodeDAO()
  val nodeRouterDAO: ChestnutNodeRouterDAO = ChestnutNodeRouterDAO()

  def verifyFlowData(req: FlowManagerSaveReq): Boolean = {
    // 1. 做完整性校验 -- 必须有开始节点和结束节点
    // 且 只能有 一个 开始节点
    lazy val startEndCompleted = req.flowData.fold(false) { xx =>
      xx.nodes.count(xxn => str2FlowNodeType(xxn.`type`).isFlowNodeTypeStart) == 1 &&
      xx.nodes.exists(xxn => str2FlowNodeType(xxn.`type`).isFlowNodeTypeEnd)
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
      val start = xx.nodes.find(xxn => str2FlowNodeType(xxn.`type`).isFlowNodeTypeStart)
      val ends = xx.nodes.filter(xxn => str2FlowNodeType(xxn.`type`).isFlowNodeTypeEnd)
      start.fold(false) { s => !xx.edges.exists(_.targetNodeId == s.id) } &&
      !ends.exists(e => xx.edges.exists(_.sourceNodeId == e.id))
    }
    // logger.info("x {},y {}, z {}", startEndCompleted, edgeCompleted, startEndLineComplete)
    startEndCompleted && edgeCompleted && startEndLineComplete
  }

  def str2FlowNodeType(str: String): FlowNodeType = {
    val ms: Map[String, FlowNodeType] = Map(
      "start"   ->  FLOW_NODE_TYPE_START,
      "user"    ->  FLOW_NODE_TYPE_USER,
      "switch"  ->  FLOW_NODE_TYPE_SWITCH,
      "end"     ->  FLOW_NODE_TYPE_END,
      "auto"    ->  FLOW_NODE_TYPE_AUTO,
      "merge"   ->  FLOW_NODE_TYPE_MERGE
    )

    ms.getOrElse(str, FLOW_NODE_TYPE_UNKNOWN)
  }

  /**
   * 保存模板
   * @param req
   * @param u
   * @return
   */
  def saveTemplate(req: FlowManagerSaveReq, u: SimpleAuthUser): DBIOAction[Long, NoStream, Effect.Read with Effect.Write] = {
    for {
      a <- templateDAO.findTemplateByFlowName1(req.metaData.fold("")(_.flowName)) map {
        case Some(v)  => v.id
        case _        => 0L
      }
      b <- if (a > 0) DBIOAction.successful(a) else templateDAO.insertTemplate(assembleTemplate(req, u))
    } yield b
  }

  def assembleTemplate(req: FlowManagerSaveReq, u: SimpleAuthUser): ChestnutTemplate =
    ChestnutTemplate(
      name = req.metaData.fold("")(_.flowName),
      isDeleted = Bool.False,
      createUser = u.id,
      createdAt = new Date().toInstant,
      updatedAt = new Date().toInstant
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

  def saveFlowData(req: FlowManagerSaveReq, u: SimpleAuthUser, templateId: Long): DBIOAction[Boolean, NoStream, Effect.Write] = {
    for {
      // 插入流程表
      a <- workFlowDAO.insertWorkFlow(assembleWorkFlow(req, u, templateId))
      // 插入流程节点表
      b <- nodeDAO.insertFlowNodes(assembleFlowNodes(req, u, a))
      // 插入路由表
      c <- nodeRouterDAO.insertFlowNodeRouters(assembleFlowNodeRouters(req, u, a, b))
    } yield a > 0 && b.nonEmpty && c.nonEmpty
  }

  def assembleWorkFlow(req: FlowManagerSaveReq, u: SimpleAuthUser, templateId: Long): ChestnutWorkFlow = ChestnutWorkFlow(
    flowName = req.metaData.fold("")(_.flowName),
    templateId = templateId,
    flowVersion = req.metaData.fold("")(_.flowVersion),
    version = 0,
    status = FlowStatus.FLOW_STATUS_NORMAL,
    description = "",
    createUser = u.id,
    updateUser = 0L,
    createdAt = new Date().toInstant,
    updatedAt = new Date().toInstant
  )

  def assembleFlowNodes(req: FlowManagerSaveReq, u: SimpleAuthUser, flowId: Long): List[ChestnutNode] = {
    val res: Seq[ChestnutNode] = req.flowData.fold(Seq.empty[ChestnutNode]) { xs =>
      xs.nodes.map { xsn =>
        ChestnutNode(
          nodeName = xsn.text.fold("")(_.value),
          nodeType = str2FlowNodeType(xsn.`type`),
          icon = "",
          nodeUuid = xsn.id,
          nodeVersion = 0,
          description = xsn.properties.fold("")(_.desc.getOrElse("")),
          flowId = flowId,
          isDeleted = Bool.False,
          createUser = u.id,
          updateUser = 0L,
          createdAt = new Date().toInstant,
          updatedAt = new Date().toInstant
        )
      }
    }

    res.toList
  }

  def assembleFlowNodeRouters(req: FlowManagerSaveReq, u: SimpleAuthUser, flowId: Long, nodes: Seq[ChestnutNode])
      : List[ChestnutNodeRouter] = {
    val res: Seq[ChestnutNodeRouter] = req.flowData.fold(Seq.empty[ChestnutNodeRouter]) { xs =>
      xs.edges.map { xse =>
        ChestnutNodeRouter(
          flowId = flowId,
          sourceNodeId = nodes.find(_.nodeUuid == xse.sourceNodeId).fold(0L)(_.id),
          targetNodeId = nodes.find(_.nodeUuid == xse.sourceNodeId).fold(0L)(_.id),
          routerRule = xse.properties.fold("")(_.script.fold("")(_.content)),
          description = xse.properties.fold("")(_.desc.getOrElse("")),
          isDeleted = Bool.False,
          createUser = u.id,
          updateUser = 0L,
          createdAt = new Date().toInstant,
          updatedAt = new Date().toInstant
        )
      }
    }

    res.toList
  }
}