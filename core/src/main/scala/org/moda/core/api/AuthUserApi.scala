package org.moda.core.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.circe.generic.auto._
import org.moda.auth.api.{Api, ApiError, Pretty}
import org.moda.auth.dao.AuthUserDAO
import org.moda.auth.middleware.TokenAuthenticate
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
  private[this] val auth: TokenAuthenticate = TokenAuthenticate(dc)

  val mainR: Route = path("main") {
    complete("Ok")
  }

  val queryR: SimpleAuthUser => Route = (u: SimpleAuthUser) =>
    path("v1" / "back" / "user" / "list") {
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
    path("v1" / "back" / "user" / IntNumber) { userId =>
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

  // userInfo的获取
  val userInfoR: SimpleAuthUser => Route = (u: SimpleAuthUser) =>
    path("v1" / "back" / "user" / "info") {
      get {
        val q = userService.userInfo(u.id)
        onComplete(q) {
          case Success(Some(value))  =>
            complete(Pretty(value))
          case Success(_) =>
            complete(ApiError.userNotExistsError)
          case Failure(exception)      =>
            // exception.printStackTrace()
            complete(ApiError.internalServerError)
        }
      }
    }

  val createR: SimpleAuthUser => Route = SimpleAuthUser =>
    path("v1" / "back" / "user") {
     post {
      entity(as[CreateUserReq]){ u =>
        val r = userDAO.createUser(AuthUser(
          username = u.username,
          email = u.email,
          password = u.password,
          isDeleted = Bool.False
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

  // 登录
  val loginR: Route = path("v0" / "user" / "login") {
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
          case Success(v) if v.user.id > 0 =>
            complete(Pretty(v))
          case Success(_) =>
            complete(ApiError.userNotFoundOrPasswordError)
          case _ =>
            complete(ApiError.internalServerError)
        }
      }
    }
  }

  // 注册用户
  val registerR: Route = path("v2" / "back" / "user" / "register") {
    auth.rootAuthenticate {param =>
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
  }

  override val authedR: Option[SimpleAuthUser => Route] = Some {
      u => queryR(u) ~ queryByIdR(u) ~ createR(u) ~ userInfoR(u)
    }

  override def publicR: Option[Route] = Some{
    mainR ~ loginR ~ registerR
  }
}
