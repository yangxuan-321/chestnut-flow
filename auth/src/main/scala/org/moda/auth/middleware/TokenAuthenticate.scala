package org.moda.auth.middleware

import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.server.Directives.{complete, onComplete, optionalHeaderValueByName, provide}
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.Logger
import io.circe.generic.auto._
import org.moda.auth.api.{ApiError, ApiStatus}
import org.moda.auth.util.JWTAuth
import org.moda.idl.SimpleAuthUser

import scala.util.{Failure, Success}
import akka.http.scaladsl.server.Directives.{complete, optionalHeaderValueByName}
import org.moda.auth.api.{ApiError, ApiStatus}
import org.moda.auth.jwt.JWTAuth
import org.moda.auth.model.Auth.UserAuthToken
import org.moda.idl.SimpleAuthUser
import com.typesafe.config.{Config, ConfigFactory}

object TokenAuthenticate {

}

trait TokenAuthenticate {
  val logger: Logger = Logger(getClass)
  val config: Config = ConfigFactory.load()
  private val headerName = "authorization"
  private[this] val key: String = config.getString("auth.server-secret")

  // 普通接口 鉴权
  def authenticate: Directive1[SimpleAuthUser] = {
    optionalHeaderValueByName(headerName).flatMap {
      case Some(v) =>
        JWTAuth.default.decode[UserAuthToken](v)(key) match {
          case Right(value) =>
          case Left(value) =>
        }
    }
  }

  // 获取用户信息
  private[this] def obtainUserInfo(ut: UserAuthToken): SimpleAuthUser = {
    userService.
  }



  private[this] def parseUserInfo(token: String): Directive1[SimpleAuthUser] = {
    token match {
      case "" => complete(ApiError(status = ApiStatus.UnKnownError, Some(s"token is empty")))
    }
  }
}
