package org.moda.core.dao

import com.typesafe.scalalogging.Logger
import org.moda.core.model.Tables
import org.moda.auth.model.tables.{AuthUserTable, UserRoleTable}
import org.moda.common.database.DatabaseComponent
import org.moda.core.model.tables.{ChestnutTemplateTable, ChestnutWorkFlowJsonTable}
import org.moda.idl.Bool._
import org.moda.idl._

import scala.concurrent.Future

/**
 * @author moda-matser
 * 2020/9/11 下午2:52
 */
object ChestnutTemplateDAO {
  def apply()(implicit dcc: DatabaseComponent): ChestnutTemplateDAO = new ChestnutTemplateDAO {
    override val dc: DatabaseComponent = dcc
  }
}

trait ChestnutTemplateDAO extends CoreDAO {
  import dc.profile.api._

  import scala.concurrent.ExecutionContext.Implicits.global

  val logger: Logger = Logger(getClass)
  val templateTable: ChestnutTemplateTable = new Tables(dc)

  def insertTemplate(x: ChestnutTemplate): Future[Long] = {
    val q = (templateTable.authUserPOs returning templateTable.authUserPOs.map(_.id)) += x
    dc.db.run(q)
  }
}
