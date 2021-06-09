package org.moda.core.api

import akka.http.scaladsl.server.Directives.{complete, get, onComplete, path, _}
import akka.http.scaladsl.server.Route
import io.circe.generic.auto._
import org.moda.auth.api.{Api, ApiError, Pretty}
import org.moda.common.database.DatabaseComponent
import org.moda.common.json.FailFastCirceSupport._
import org.moda.common.json.PbJsonExtendSupport._
import org.moda.core.service.BackFlowService
import org.moda.idl.SimpleAuthUser

import scala.util.{Failure, Success}

object BackFlowApi {

  def apply()(implicit dc: DatabaseComponent): BackFlowApi = new BackFlowApi()

}

class BackFlowApi(implicit dc: DatabaseComponent) extends Api {
  val backFlowService: BackFlowService = BackFlowService()

  override def publicR: Option[Route] = None
}
