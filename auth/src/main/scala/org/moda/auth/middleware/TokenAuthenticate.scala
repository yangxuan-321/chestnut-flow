package org.moda.auth.middleware

import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.server.Directives._
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.Logger
import io.circe.generic.auto._
import org.moda.auth.api.{ApiError, ApiStatus}
import org.moda.auth.jwt.JWTAuth.JwtExpired
import org.moda.auth.model.Auth.UserAuthToken
import org.moda.auth.service.UserService
import org.moda.common.database.DatabaseComponent
import org.moda.common.json.FailFastCirceSupport._
import org.moda.idl.SimpleAuthUser

object TokenAuthenticate {
  def apply(implicit dc: DatabaseComponent): TokenAuthenticate = new TokenAuthenticate()
}

class TokenAuthenticate(implicit dc: DatabaseComponent) {
  // 导入ExecutionContext
  import scala.concurrent.ExecutionContext.Implicits.global
  val logger: Logger = Logger(getClass)
  private val headerName = "authorization"
  val config: Config = ConfigFactory.load().getConfig("auth.root-key")
  private[this] val rootAuthKeyName: String = config.getString("name")
  private[this] val rootAuthKeyValue: String = config.getString("value")
  private val userService = UserService()

  // 普通接口 鉴权
  def authenticate: Directive1[SimpleAuthUser] = {
    optionalHeaderValueByName(headerName).flatMap {
      case Some(v) =>
        userService.tokenDecode(v) match {
          case Right(r)         => obtainUserInfo(r)
          case Left(JwtExpired) => complete(ApiError.tokenExpired)
          case _                => complete(ApiError.tokenInvalid)
        }
      case _ => complete(ApiError.tokenInvalid)
    }
  }

  // 获取用户信息
  private[this] def obtainUserInfo(ut: UserAuthToken): Directive1[SimpleAuthUser] = {
    val u = userService.queryUserById(ut.userId).map(_.map { x =>
      SimpleAuthUser(
        id = x.id,
        username = x.username,
        email = x.email
      )
    })

    onComplete(u) flatMap {
      case util.Success(Some(user)) => provide(user)
      case util.Success(_) => complete(ApiError(status = ApiStatus.UserNotExistsError))
      case _ => complete(ApiError(status = ApiStatus.InternalServerError))
    }
  }

  private[this] def parseUserInfo(token: String): Directive1[SimpleAuthUser] = {
    token match {
      case "" => complete(ApiError(status = ApiStatus.UnKnownError, Some(s"token is empty")))
    }
  }

  def rootAuthenticate: Directive1[Boolean] = {
    optionalHeaderValueByName(rootAuthKeyName).flatMap {
      case Some(v) if v == rootAuthKeyValue   =>  provide(true)
      case _                                  =>  complete(ApiError.forbiddenError)
    }
  }
}
