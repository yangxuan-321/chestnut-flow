package org.moda.core.service

import com.typesafe.scalalogging.Logger
import com.zz.cdp.common.util.TimeTransUtil
import org.moda.auth.dao.AuthUserDAO
import org.moda.common.database.DatabaseComponent
import org.moda.core.bo.BackFlowBO
import org.moda.core.dao.{ChestnutTemplateDAO, ChestnutWorkFlowJsonDAO}
import org.moda.idl.{ChestnutTemplate, ChestnutTemplateVO, ChestnutWorkFlowJson, FlowManagerListReq, FlowManagerSaveReq, SimpleAuthUser}

import scala.concurrent.Future

object BackFlowService {
  def apply()(implicit dc: DatabaseComponent): BackFlowService = new BackFlowService()
}

class BackFlowService(implicit dc: DatabaseComponent) {

  import DatabaseComponent.profile.api._
  import scala.concurrent.ExecutionContext.Implicits.global
  val logger: Logger = Logger(getClass)
  val backFlowBO: BackFlowBO = BackFlowBO()
  val templateDAO: ChestnutTemplateDAO = ChestnutTemplateDAO()
  val authUserDAO: AuthUserDAO = AuthUserDAO()
  val chestnutWorkFlowJsonDAO: ChestnutWorkFlowJsonDAO = ChestnutWorkFlowJsonDAO()

  def saveFlow(req: FlowManagerSaveReq, u: SimpleAuthUser): Future[Either[String, Boolean]] = {
    // 1.判断流程数据的合法性
    val verify = backFlowBO.verifyFlowData(req)

    // 2.保存数据
    if (verify) {
      val action = for {
        a <- backFlowBO.saveTemplate(req, u)
        b <- backFlowBO.saveFlowJson(req, a)
        // c <- if (b) backFlowBO.saveFlowData(req, u) else Future{false}
      } yield a > 0 && b > 0
      // transactionally事物
      dc.db.run(action.transactionally).map(if (_) Right(true) else Left("保存出错"))
    } else Future {Left("流程数据格式不正确")}

  }

  def validateFlowName(flowName: String): Future[Boolean] = {
    templateDAO.findTemplateByFlowName(flowName).map(_.isEmpty)
  }

  def listFlow(req: FlowManagerListReq): Future[Seq[ChestnutTemplateVO]] = {
    for {
      a <- templateDAO.listFlowTemplate(req)
      b <- Future { (a.map(_.createUser) ++ a.map(_.updateUser)).distinct }
      c <- authUserDAO.queryByIds(b.toList)
    } yield {
      a.map {ax =>
        ChestnutTemplateVO(
          id = ax.id,
          name = ax.name,
          createUser = ax.createUser,
          updateUser = ax.updateUser,
          createUserName = c.find(_.id == ax.createUser).fold("")(_.username),
          updateUserName = c.find(_.id == ax.updateUser).fold("")(_.username),
          createdAt = TimeTransUtil.instantToTimeString(ax.createdAt),
          updatedAt = TimeTransUtil.instantToTimeString(ax.updatedAt)
        )
      }
    }
  }

  def detailFlow(templateId: Int): Future[Seq[ChestnutWorkFlowJson]] = {
    chestnutWorkFlowJsonDAO.detailFlow(templateId)
  }
}