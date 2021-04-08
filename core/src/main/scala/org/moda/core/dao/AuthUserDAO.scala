package org.moda.core.dao

import com.typesafe.scalalogging.Logger
import org.moda.core.database.DatabaseComponent
import org.moda.core.model.Tables
import org.moda.core.model.tables.AuthUserTable
import org.moda.idl.AuthUser

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

trait AuthUserDAO extends DAO {
  import dc.profile.api._

  val logger: Logger = Logger(getClass)
  val authUserTable: AuthUserTable = new Tables(dc)

  def query(): Future[Seq[AuthUser]] = {
    val q = authUserTable.authUserPOs.result
    logger.info("sql: {}", q.statements.mkString(","))
    dc.db.run(q)
  }
}
