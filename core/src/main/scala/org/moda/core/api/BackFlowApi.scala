package org.moda.core.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.circe.generic.auto._
import io.circe.syntax._
import org.moda.auth.api.{Api, ApiError, ApiStatus, Pretty}
import org.moda.common.database.DatabaseComponent
import org.moda.common.json.FailFastCirceSupport._
import org.moda.common.json.PbJsonExtendSupport._
import org.moda.core.service.BackFlowService
import org.moda.idl._
import com.typesafe.scalalogging.Logger

import scala.concurrent.Future
import scala.util._

object BackFlowApi {

  def apply()(implicit dc: DatabaseComponent): BackFlowApi = new BackFlowApi()

}

class BackFlowApi(implicit dc: DatabaseComponent) extends Api {
  import scala.concurrent.ExecutionContext.Implicits.global
  val logger: Logger = Logger(getClass)

  val backFlowService: BackFlowService = BackFlowService()

  val saveFlowR: SimpleAuthUser => Route = (u: SimpleAuthUser) =>
    path("v1" / "flow" / "manager") {
      post {
        entity(as[FlowManagerSaveReq]){ req =>
          logger.info("流程保存: {}", req.asJson)
          val r = backFlowService.saveFlow(req, u)
          onComplete(r) {
            case Success(Right(v)) =>
              complete(Pretty(v))
            case Success(Left(v)) =>
              complete(new ApiError(ApiStatus.InternalServerError, Some(v)))
            case _ =>
              complete(ApiError.internalServerError)
          }
        }
      }
    }

  val validateFlowNameR: SimpleAuthUser => Route = (u: SimpleAuthUser) =>
    path("v1" / "flow" / "manager" / "validateFlowName" / Remaining) { flowName =>
      get {
        logger.info("校验流程名称: {}", flowName)
        val r = backFlowService.validateFlowName(flowName)
        onComplete(r) {
          case Success(v) if v =>
            complete(Pretty(v))
          case _ =>
            complete(ApiError.internalServerError.copy(message = Some("名称重复")))
        }
      }
    }

  val listFlowR: SimpleAuthUser => Route = (u: SimpleAuthUser) =>
    path("v1" / "flow" / "manager" / "list") {
      post {
        entity(as[FlowManagerListReq]){ req =>
          logger.info("流程列表查询: {}", req.asJson)
          val r = backFlowService.listFlow(req)
          val r = Future { req }
          onComplete(r) {
            case Success(v) =>
              complete(Pretty(v))
            case _ =>
              complete(ApiError.internalServerError)
          }
        }
      }
    }

  override def publicR: Option[Route] = None

  override def authedR: Option[SimpleAuthUser => Route] = Some {
    u => saveFlowR(u) ~ validateFlowNameR(u) ~ listFlowR(u)
  }
}
