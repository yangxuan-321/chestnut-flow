package org.moda.auth.service

import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.Logger
import io.circe.generic.auto._
import org.moda.auth.dao.AuthUserDAO
import org.moda.auth.jwt.JWTAuth
import org.moda.auth.model.Auth.UserAuthToken
import org.moda.common.database.DatabaseComponent
import org.moda.common.util.MessageDigest
import org.moda.idl.{AuthUser, LoginForm, RegisterUserInfo, SimpleAuthUser}

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

  def registerUser(f: RegisterUserInfo): Future[Either[String, Boolean]] = {
    for {
      u <- userDAO.findUserByUsernameOrEmail(f.username)
      b <- u match {
        case Some(v) if f.username == v.username  =>  Future { Left("用户名已经存在") }
        case Some(v) if f.email == v.email        =>  Future { Left("邮箱已经重复") }
        case Some(_)                              =>  Future { Left("用户已经被注册") }
        case None                                 =>
          userDAO.createUser(AuthUser(
            username = f.username,
            email = f.email,
            password = MessageDigest.md5AsHex(f.password)
          )).map(Right(_))
        case _                                    =>  Future { Left("未知错误") }
      }
    } yield b
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
