package org.moda.core.service

import com.typesafe.scalalogging.Logger
import org.moda.common.database.DatabaseComponent
import org.moda.core.bo.BackFlowBO
import org.moda.idl.{FlowManagerSaveReq, SimpleAuthUser}
import slick.ast.Ordering.{Asc, Desc, Direction}
import slick.lifted.{CanBeQueryCondition, Query}
import scala.concurrent.Future

object BackFlowService {
  def apply()(implicit dc: DatabaseComponent): BackFlowService = new BackFlowService()
}

class BackFlowService(implicit dc: DatabaseComponent) {
  val logger: Logger = Logger(getClass)
  import scala.concurrent.ExecutionContext.Implicits.global
  val backFlowBO: BackFlowBO = BackFlowBO()

  def saveFlow(req: FlowManagerSaveReq, u: SimpleAuthUser): Future[Either[String, Boolean]] = {
    // 1.判断流程数据的合法性
    val verify = backFlowBO.verifyFlowData(req)

    // 2.保存数据
    if (verify) {
      val action = for {
        a <- backFlowBO.saveTemplate(req, u)
        b <- backFlowBO.saveFlowJson(req, a)
        // c <- if (b) backFlowBO.saveFlowData(req, u) else Future{false}
      } yield (a, b)
      dc.db.run(action.transactionally).map(a > 0 && b > 0)
      res.map(if (_) Right(true) else Left("保存出错"))
    } else Future {Left("流程数据格式不正确")}

  }
}