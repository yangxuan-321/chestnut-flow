package org.moda.core.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.circe.generic.auto._
import org.moda.auth.api.{Api, Pretty}
import org.moda.common.json.FailFastCirceSupport._
import org.moda.core.dao.AuthUserDAO
import org.moda.core.database.DatabaseComponent
import org.moda.idl.{AuthUser, CreateUserReq}
import org.moda.auth.api

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
          val res = Pretty(value)
          complete(res)
        case Failure(exception)      =>
          // exception.printStackTrace()
          complete("failure")
      }
    }
  }

  val queryByIdR: Route = path("v1" / "user" / IntNumber) { userId =>
    get {
      val q = dao.queryById(userId)
      onComplete(q) {
        case Success(value)  =>
          value.fold(complete("{}"))(x => complete(Pretty(x)))
        case Failure(exception)      =>
          // exception.printStackTrace()
          complete("failure")
      }
    }
  }

  def createR: Route = path("v1" / "user") {
    post {
      entity(as[CreateUserReq]){ user =>
        val r = dao.createUser(user);
        onComplete(r) {
          case Success(v) if v =>
            complete(Pretty(v))
          case _ =>
            complete("failure")
        }
      }
    }
  }

  override val authedR: Route = mainR ~ queryR ~ queryByIdR //~ createR
}
