package org.moda.core.service

import cats.data.OptionT
import cats.implicits._
import com.typesafe.scalalogging.Logger
import org.moda.common.database.DatabaseComponent
import org.moda.core.bo.BackFlowBO
import org.moda.idl.{FlowManagerSaveReq, SimpleAuthUser}

import scala.concurrent.Future
import scala.util.Random

object BackFlowService {
  def apply()(implicit dc: DatabaseComponent): BackFlowService = new BackFlowService()
}

class BackFlowService(implicit dc: DatabaseComponent) {

  val logger: Logger = Logger(getClass)
  import scala.concurrent.ExecutionContext.Implicits.global
  val backFlowBO: BackFlowBO = BackFlowBO()

  def saveFlow(req: FlowManagerSaveReq, u: SimpleAuthUser): Future[Either[String, Boolean]] = {
    Thread.sleep(3000)
    // 1.判断流程数据的合法性
    val verify = backFlowBO.verifyFlowData(req)

    // 2.保存数据
    if (verify) {
      val res = for {
        a <- backFlowBO.saveTemplate(req, u)
        b <- if (a > 0) backFlowBO.saveFlowJson(req, a) else Future{false}
        c <- if (b) backFlowBO.saveFlowData(req, u) else Future{false}
      } yield a > 0 && b && c

      res.map(if (_) Right(true) else Left("保存出错"))
    } else Future {Left("流程数据格式不正确")}

  }
}