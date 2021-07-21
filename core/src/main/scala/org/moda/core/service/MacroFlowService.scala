package org.moda.core.service

import cats.data.OptionT
import cats.implicits._
import com.typesafe.scalalogging.Logger
import org.moda.common.database.DatabaseComponent
import org.moda.core.bo.MacroFlowBO
import org.moda.core.dao.{ChestnutNodeDAO, ChestnutWorkFlowDAO}
import org.moda.idl.{SimpleAuthUser, WorkFlowServiceStartReq}

import scala.concurrent.Future

object MacroFlowService {
  def apply()(implicit dc: DatabaseComponent): MacroFlowService = new MacroFlowService()
}

class MacroFlowService(implicit dc: DatabaseComponent) {

  import scala.concurrent.ExecutionContext.Implicits.global
  val logger: Logger = Logger(getClass)
  val chestnutWorkFlowDAO: ChestnutWorkFlowDAO = ChestnutWorkFlowDAO()
  val chestnutNodeDAO: ChestnutNodeDAO = ChestnutNodeDAO()
  val macroFlowBO: MacroFlowBO = MacroFlowBO()

  def startWorkFlow(req: WorkFlowServiceStartReq, u: SimpleAuthUser): Future[Either[String, Boolean]] = {
    val r = for {
      // 1. uuid查询流程
      a <- OptionT(chestnutWorkFlowDAO.queryWorkFlowByFlowUuid(req.flowUuid))
      // 2. 查询流程对应的 开始节点
      b <- OptionT(chestnutNodeDAO.queryStartNodeByFlowId(a.id))
      // 3. 将流程数据插入流程实例表 并且 将开始节点插入到流程节点实例表
      c <- OptionT.liftF {
        macroFlowBO.saveWorkFlowStartData(req, u, a, b)
      }
    } yield c

    r.getOrElse(Left("保存失败"))
  }

}