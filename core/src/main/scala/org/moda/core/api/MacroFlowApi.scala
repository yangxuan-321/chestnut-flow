package org.moda.core.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.circe.generic.auto._
import io.circe.syntax._
import org.moda.auth.api.{Api, ApiError, ApiStatus, Pretty}
import org.moda.common.database.DatabaseComponent
import org.moda.common.json.FailFastCirceSupport._
import org.moda.common.json.PbJsonExtendSupport._
import org.moda.core.service.{BackFlowService, MacroFlowService}
import org.moda.idl._
import com.typesafe.scalalogging.Logger

import scala.concurrent.Future
import scala.util._

/**
 * 对于向外提供服务的api
 */
object MacroFlowApi {

  def apply()(implicit dc: DatabaseComponent): MacroFlowApi = new MacroFlowApi()

}

class MacroFlowApi(implicit dc: DatabaseComponent) extends Api {
  import scala.concurrent.ExecutionContext.Implicits.global
  val logger: Logger = Logger(getClass)

  val macroFlowService: MacroFlowService = MacroFlowService()

  val startWorkFlowR: SimpleAuthUser => Route = (u: SimpleAuthUser) =>
    path("v2" / "service" / "workflow" / "start" ) {
      post {
        entity(as[WorkFlowServiceStartReq]){ req =>
          logger.info("启动流程: {}", req.asJson)
          val r = macroFlowService.startWorkFlow(req, u)
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

  override def publicR: Option[Route] = None

  override def authedR: Option[SimpleAuthUser => Route] = Some {
    u => startWorkFlowR(u)
  }
}
