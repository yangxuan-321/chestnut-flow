package org.moda.auth.dao

import com.typesafe.scalalogging.Logger
import org.moda.auth.model.Tables
import org.moda.auth.model.tables.{AuthUserTable, UserRoleTable}
import org.moda.common.database.DatabaseComponent
import org.moda.idl.Bool._
import org.moda.idl._

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
      .filter(_.isDeleted === (False: Bool))
      .result
    logger.info("query: {}", q.statements.mkString(","))
    dc.db.run(q)
  }

  def queryById(userId: Long): Future[Option[AuthUser]] = {
    val q = authUserTable
      .authUserPOs
      .filter(_.isDeleted === (False: Bool))
      .filter(_.id === userId)
      .take(1)
      .result
    logger.info("queryById: {}", q.statements.mkString(","))
    dc.db.run(q).map(_.headOption)
  }

  def createUser(u: AuthUser): Future[Boolean] = {
    val q = authUserTable.authUserPOs += u
    dc.db.run(q).map(_ > 0)
  }

  def findUserByUsernameOrEmail(uu: String): Future[Option[AuthUser]] = {
    val q = authUserTable
      .authUserPOs
      .filter(_.isDeleted === (False: Bool))
      .filter {xx => xx.username === uu || xx.email === uu }
      .take(1)
      .result

    logger.info("findUserByUsernameOrEmail: {}", q.statements.mkString(","))
    dc.db.run(q).map(_.headOption)
  }

  def queryUserInfoById(userId: Long): Future[Option[(AuthUser, UserRole)]] = {
    val q = authUserTable.authUserPOs
      .filter(_.id === userId)
      .filter(_.isDeleted === (False: Bool))
      .join(userRoleTable.userRolePOs)
      .on(_.id === _.userId)
      .take(1)
      .result
    logger.info("queryUserInfoById: {}", q.statements.mkString(","))
    dc.db.run(q).map(_.headOption)
  }

  def queryByIds(userIds: List[Long]): Future[Seq[AuthUser]] = {
    val q = authUserTable
      .authUserPOs
      .filter(_.isDeleted === (False: Bool))
      .filter(_.id.inSetBind(userIds))
      .result
    logger.info("queryByIds: {}", q.statements.mkString(","))
    dc.db.run(q)
  }

  def queryByRoleType(roles: List[RoleType]): Future[Option[AuthUser]] = {
    val q = authUserTable
      .authUserPOs
      .filter(_.isDeleted === (False: Bool))
      .join(userRoleTable.userRolePOs.filter(_.roleType inSetBind roles))
      .on(_.id === _.userId)
      .take(1)
      .map(_._1)
      .result
    logger.info("queryByRoleType: {}", q.statements.mkString(","))
    dc.db.run(q).map(_.headOption)
  }
}
