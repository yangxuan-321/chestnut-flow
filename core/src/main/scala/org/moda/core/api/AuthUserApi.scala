package org.moda.core.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.circe.generic.auto._
import org.moda.auth.api.{Api, ApiError, Pretty}
import org.moda.auth.dao.AuthUserDAO
import org.moda.auth.model.Auth.UserAuthToken
import org.moda.auth.service.UserService
import org.moda.common.database.DatabaseComponent
import org.moda.common.json.FailFastCirceSupport._
import org.moda.common.json.PbJsonExtendSupport._
import org.moda.idl._
import scala.concurrent.Future
import scala.util.{Failure, Success}



object AuthUserApi {

  def apply()(implicit dc: DatabaseComponent): AuthUserApi = new AuthUserApi()

}

/**
 * @author moda-matser
 * 2020/9/4 下午4:33
 */
class AuthUserApi(implicit dc: DatabaseComponent) extends Api {
  import cats.data._
  import cats.implicits._

  import scala.concurrent.ExecutionContext.Implicits.global
  val userDAO: AuthUserDAO = AuthUserDAO()
  val userService: UserService = UserService()

  val mainR: Route = path("main") {
    complete("Ok")
  }

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

  val queryByIdR: SimpleAuthUser => Route = SimpleAuthUser =>
    path("v1" / "user" / IntNumber) { userId =>
      get {
        val q = userDAO.queryById(userId)
        onComplete(q) {
          case Success(value)  =>
            complete(Pretty(value))
          case Failure(exception)      =>
            // exception.printStackTrace()
            complete(ApiError.internalServerError)
        }
      }
    }

  val createR: SimpleAuthUser => Route = SimpleAuthUser =>
    path("v1" / "user") {
     post {
      entity(as[CreateUserReq]){ u =>
        val r = userDAO.createUser(AuthUser(
          username = u.username,
          email = u.email,
          password = u.password,
          isDelete = Bool.False
        ))
        onComplete(r) {
          case Success(v) if v =>
            complete(Pretty(v))
          case _ =>
            complete(ApiError.internalServerError)
        }
      }
     }
    }

  val loginR: Route = path("v1" / "user" / "login") {
    post {
      entity(as[LoginForm]) { params =>
        val res: OptionT[Future, LoginResult] = for {
          u <- OptionT(userService.login(params))
          t <- OptionT.liftF {Future {
              userService.tokenEncode(UserAuthToken(userId = u.id))
          }}
        } yield LoginResult(
          user = u,
          token = t
        )

        val r = res.getOrElse(LoginResult())
        onComplete(r) {
          case Success(v) =>
            complete(Pretty(v))
          case _ =>
            complete(ApiError.internalServerError)
        }
      }
    }
  }

  val registerR: Route = path("v1" / "user" / "register") {
    post {
      entity(as[RegisterUserInfo]) { params =>
        val res = userService.registerUser(params)
        onComplete(res) {
          case Success(Left(v)) =>
            complete(ApiError.internalServerError.copy(message = Some(v)))
          case Success(Right(false)) =>
            complete(ApiError.internalServerError.copy(message = Some("注册失败")))
          case Success(Right(true)) =>
            complete(Pretty(true))
          case _ =>
            complete(ApiError.internalServerError)
        }
      }
    }
  }

  override val authedR: SimpleAuthUser => Route =
    u => queryR(u) ~ queryByIdR(u) ~ createR(u)

  override def publicR: Route = mainR ~ loginR ~ registerR
}
