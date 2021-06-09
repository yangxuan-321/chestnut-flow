package org.moda.auth.dao

import com.typesafe.scalalogging.Logger
import org.moda.auth.model.Tables
import org.moda.common.database.DatabaseComponent
import org.moda.auth.model.tables.{AuthUserTable, UserRoleTable}
import org.moda.idl.Bool._
import org.moda.idl.{CreateUserReq, _}
import reactivemongo.api.commands

import scala.concurrent.Future

/**
 * @author moda-matser
 * 2020/9/11 下午2:52
 */
object AuthUserDAO {
  def apply()(implicit dcc: DatabaseComponent): AuthUserDAO = new AuthUserDAO {
    override val dc: DatabaseComponent = dcc
  }
}

trait AuthUserDAO extends AuthDAO {
  import dc.profile.api._

  import scala.concurrent.ExecutionContext.Implicits.global

  val logger: Logger = Logger(getClass)
  val authUserTable: AuthUserTable = new Tables(dc)
  val userRoleTable: UserRoleTable = new Tables(dc)

  def query(): Future[Seq[AuthUser]] = {
    val q = authUserTable
      .authUserPOs
      .filter(_.isDelete === (False: Bool))
      .result
    logger.info("sql: {}", q.statements.mkString(","))
    dc.db.run(q)
  }

  def queryById(userId: Long): Future[Option[AuthUser]] = {
    val q = authUserTable
      .authUserPOs
      .filter(_.isDelete === (False: Bool))
      .filter(_.id === userId)
      .take(1)
      .result
    logger.info("sql: {}", q.statements.mkString(","))
    dc.db.run(q).map(_.headOption)
  }

  def createUser(u: AuthUser): Future[Boolean] = {
    val q = authUserTable.authUserPOs += u
    dc.db.run(q).map(_ > 0)
  }

  def findUserByUsernameOrEmail(uu: String): Future[Option[AuthUser]] = {
    val q = authUserTable
      .authUserPOs
      .filter(_.isDelete === (False: Bool))
      .filter {xx => xx.username === uu || xx.email === uu }
      .take(1)
      .result

    logger.info("findUserByUsernameOrEmail: {}", q.statements.mkString(","))
    dc.db.run(q).map(_.headOption)
  }

  def queryUserInfoById(userId: Long): Future[Option[(AuthUser, UserRole)]] = {
    val q = authUserTable.authUserPOs
      .filter(_.id === userId)
      .filter(_.isDelete === (False: Bool))
      .join(userRoleTable.userRolePOs)
      .on(_.id === _.userId)
      .take(1)
      .result
    logger.info("sql: {}", q.statements.mkString(","))
    dc.db.run(q).map(_.headOption)
  }
}
