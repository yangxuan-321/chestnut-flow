package org.moda.core.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.circe.generic.auto._
import org.moda.common.json.Json2String._
import org.moda.common.model.Pretty
import org.moda.core.dao.AuthUserDAO
import org.moda.core.database.DatabaseComponent
import org.moda.idl.{AuthUser, CreateUserReq}

import scala.util._



object AuthUserApi {

  def apply()(implicit dc: DatabaseComponent): AuthUserApi = new AuthUserApi()

}

/**
 * @author moda-matser
 * 2020/9/4 下午4:33
 */
class AuthUserApi(implicit dc: DatabaseComponent) extends Api {

  val dao: AuthUserDAO = AuthUserDAO()

  val mainR: Route = path("main") {
    complete("Ok")
  }

  val queryR: Route = path("v1" / "user" / "list") {
    get {
      val q = dao.query()
      onComplete(q) {
        case Success(value)  =>
          val res: Pretty[Seq[AuthUser]] = new Pretty[Seq[AuthUser]](200, "success", value)
          complete(res.toJsonString)
        case Failure(exception)      =>
          // exception.printStackTrace()
          complete("failure")
      }
    }
  }

  def createR: Route = path("v1" / "user") {
    post {
      entity(as[CreateUserReq]){

      }
    }
  }

  override val routes: Route = mainR ~ queryR ~

}
