package org.moda.auth.service

import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.Logger
import io.circe.generic.auto._
import org.moda.auth.dao.AuthUserDAO
import org.moda.auth.jwt.JWTAuth
import org.moda.auth.model.Auth.UserAuthToken
import org.moda.common.database.DatabaseComponent
import org.moda.common.util.MessageDigest
import org.moda.idl.{AuthUser, LoginForm, SimpleAuthUser}

import scala.concurrent.Future


object UserService {
  def apply()(implicit dc: DatabaseComponent): UserService = new UserService {
    val userDAO: AuthUserDAO = AuthUserDAO()
  }

}

trait UserService {
  import scala.concurrent.ExecutionContext.Implicits.global
  val config: Config = ConfigFactory.load()
  private[this] val key: String = config.getString("auth.server-secret")

  val logger: Logger = Logger(getClass)

  def userDAO: AuthUserDAO

  /** 根据用户id获取用户并且更新最新访问时间 **/
  def queryUserById(userId: Long): Future[Option[AuthUser]] =
    userDAO.queryById(userId)

  def login(f: LoginForm): Future[Option[SimpleAuthUser]] = {
    userDAO.findUserByUsernameOrEmail(f.username) map {
      case Some(u) if validatePassword(f.password, u.password) =>
        Some(SimpleAuthUser(id = u.id, username = u.username, email = u.email))
      case _ => Option.empty[SimpleAuthUser]
    }
  }

  def validatePassword(raw: String, hash: String): Boolean = {
    MessageDigest.md5AsHex(raw) == hash
  }

  def tokenDecode(token: String): Either[JWTAuth.JWTAuthResult, UserAuthToken] = {
    JWTAuth.default.decode[UserAuthToken](token)(key)
  }

  def tokenEncode(u: UserAuthToken): String = {
    JWTAuth.default.encode[UserAuthToken](u)(key)
  }
}
