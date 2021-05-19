package org.moda.core.api

import akka.http.scaladsl.server.Directives.{complete, get, onComplete, path, _}
import akka.http.scaladsl.server.Route
import io.circe.generic.auto._
import org.moda.auth.api.{Api, ApiError, Pretty}
import org.moda.common.database.DatabaseComponent
import org.moda.common.json.FailFastCirceSupport._
import org.moda.core.service.BackFlowService
import org.moda.idl.SimpleAuthUser

import scala.util.{Failure, Success}

object BackFlowApi {

  def apply()(implicit dc: DatabaseComponent): BackFlowApi = new BackFlowApi()

}

class BackFlowApi(implicit dc: DatabaseComponent) extends Api {
  val backFlowService: BackFlowService = BackFlowService()
  val typeGroupsR: SimpleAuthUser => Route = (u: SimpleAuthUser) =>
    path("v1" / "back" / "type-groups") {
      get {
        val q = backFlowService.typeGroups()
        onComplete(q) {
          case Success(value)  =>
            val res = Pretty(value)
            complete(res)
          case Failure(exception)      =>
            // exception.printStackTrace()
            complete(ApiError.internalServerError)
        }
      }
    }

  override def authedR: SimpleAuthUser => Route = typeGroupsR

  override def publicR: Route = ???
}
