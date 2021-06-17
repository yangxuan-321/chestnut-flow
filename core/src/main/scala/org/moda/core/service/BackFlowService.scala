package org.moda.core.service

import java.io.File

import com.typesafe.scalalogging.Logger
import io.circe.generic.auto._
import io.circe.parser._
import org.moda.common.database.DatabaseComponent
import org.moda.core.bo.BackFlowBO
import org.moda.idl.{FlowManagerSaveReq, SimpleAuthUser}
import reactivemongo.api.DB

import scala.concurrent.Future
import scala.io.Source
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
    if (verify && Random.nextInt(2)%2 == 0) {
      Future.successful(Right(true))
    } else Future {Left("数据有误")}
  }
}