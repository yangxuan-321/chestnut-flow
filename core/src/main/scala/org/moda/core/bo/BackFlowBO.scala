package org.moda.core.bo

import com.typesafe.scalalogging.Logger
import org.moda.common.database.DatabaseComponent
import org.moda.idl.BackFlowNodeType._
import org.moda.idl.{BackFlowNodeType, FlowManagerSaveReq, SimpleAuthUser}

object BackFlowBO {
  def apply()(implicit dc: DatabaseComponent): BackFlowBO = new BackFlowBO()
}

class BackFlowBO(implicit dc: DatabaseComponent) {
  val logger: Logger = Logger(getClass)

//  def saveFlow(req: FlowManagerSaveReq, u: SimpleAuthUser) = {
//
//  }

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
}