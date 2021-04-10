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

object TokenAuthenticate {

}

trait TokenAuthenticate {
  val logger: Logger = Logger(getClass)
  val config: Config = ConfigFactory.load()
  private val headerName = "authorization"
  private[this] val key: String = config.getString("auth.server-secret")

//  def authenticate: Directive1[SimpleAuthUser] = {
//    optionalHeaderValueByName(headerName).flatMap { x =>
//      x.getOrElse("")
//    }
//  }
//
//  private[this] def parseUserInfo(token: String): Directive1[SimpleAuthUser] = {
//    token match {
//      case "" => complete(ApiError(status = ApiStatus.Forbidden, Some(s"token is empty")))
//      case t => JWTAuth.default.decode[UserAuthToken](t)(key) match {
//        case Right(u) =>
//          onComplete(userService.getUserAndUpdateAcvite(u.userId, u.code)).flatMap {
//            case Success(Some(user)) if user.status == Status.Inactive =>
//              logger.error("user is inactive, userId:{}, code:{}", u.userId, u.code)
//              complete(ApiError(status = ApiStatus.CheatError, Some(cheatTip)))
//            case Success(Some(user)) =>
//              logger.debug("user authenticated success. userId: {}, code: {}, userName: {}", user.id, u.code, user.nickName)
//              provide(user)
//            case Success(_) =>
//              logger.error("user not exist, userId: {}, code: {}, token: {}", u.userId, u.code, token)
//              complete(ApiError(status = ApiStatus.UserNotExistsError, Some(s"user not exist userId:${u.userId}, code:${u.code}")))
//            case Failure(e) =>
//              logger.error("get user error, userId: {}, code: {}, exception: {} , token: {}", u.userId, u.code, e.toString, token)
//              complete(ApiError(status = ApiStatus.UnKnowError, Some(s"get user failure, userId:${u.userId}, code:${u.code}")))
//          }
//        case Left(JWTAuth.JwtExpired) =>
//          logger.error("decode token JwtExpired , token: {}", token)
//          complete(ApiError.tokenExpired)
//        case Left(a) =>
//          logger.error("decode token error: {} , token: {}", a, token)
//          complete(ApiError(status = ApiStatus.UnKnowError, Some(s"decode token error, receive token: $token")))
//      }
//    }
//  }
}

final case class UserAuthToken(
                                userId: Long
                                , code: Option[String] = None
)
