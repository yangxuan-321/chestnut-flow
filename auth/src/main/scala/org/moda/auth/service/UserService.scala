package org.moda.auth.service

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.Logger
import org.moda.auth.dao.AuthUserDAO
import org.moda.common.database.DatabaseComponent
import org.moda.idl.AuthUser

import scala.concurrent.Future


object UserService {
  def apply()(implicit dc: DatabaseComponent): UserService = new UserService {

    val config: Config = ConfigFactory.load()

    val userDAO: AuthUserDAO = AuthUserDAO()
  }

}

trait UserService {

  val logger: Logger = Logger(getClass)

  def userDAO: AuthUserDAO

  /** 根据用户id获取用户并且更新最新访问时间 **/
  def queryUserById(userId: Long): Future[Option[AuthUser]] =
    userDAO.queryById(userId)
}
