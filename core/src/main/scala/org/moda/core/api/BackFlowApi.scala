package org.moda.core.api

import akka.http.scaladsl.server.Directives.{complete, get, onComplete, path}
import akka.http.scaladsl.server.Route
import org.moda.auth.api.{Api, ApiError, Pretty}
import org.moda.common.database.DatabaseComponent
import org.moda.idl.SimpleAuthUser

import scala.util.{Failure, Success}

object BackFlowApi {

  def apply()(implicit dc: DatabaseComponent): BackFlowApi = new BackFlowApi()

}

class BackFlowApi(implicit dc: DatabaseComponent) extends Api {
  val queryR: SimpleAuthUser => Route = (u: SimpleAuthUser) =>
    path("v1" / "user" / "list") {
      get {
        val q = userDAO.query()
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
}
