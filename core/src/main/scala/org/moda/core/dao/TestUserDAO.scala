package org.moda.core.dao

import com.typesafe.scalalogging.Logger
import org.moda.core.database.DatabaseComponent
import org.moda.core.model.Tables
import org.moda.core.model.tables.TestUserTable
import org.moda.idl.TestUserPO

import scala.concurrent.Future

/**
 * @author moda-matser
 * 2020/9/11 下午2:52
 */
object TestUserDAO {
  def apply()(implicit dcc: DatabaseComponent): TestUserDAO = new TestUserDAO {
    override val dc: DatabaseComponent = dcc
  }
}

trait TestUserDAO extends DAO {

  import dc.profile.api._

  val logger: Logger = Logger(getClass)

  val testUserTable: TestUserTable = new Tables(dc)

  def query(): Future[Seq[TestUserPO]] = {
    val q = testUserTable.testUserPOs.result
//    logger.info("sql: {}", q.statements.mkString(","))
    dc.db.run(q)
  }


}
