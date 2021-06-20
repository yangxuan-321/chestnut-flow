package org.moda.core.dao

import com.typesafe.scalalogging.Logger
import org.moda.common.database.DatabaseComponent
import org.moda.core.model.Tables
import org.moda.core.model.tables.ChestnutTemplateTable
import org.moda.idl.Bool.False
import org.moda.idl._
import slick.sql.FixedSqlAction

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

  val logger: Logger = Logger(getClass)
  val templateTable: ChestnutTemplateTable = new Tables(dc)

  def insertTemplate(x: ChestnutTemplate): FixedSqlAction[Long, NoStream, Effect.Write] = {
    val q = (templateTable.templatePOs returning templateTable.templatePOs.map(_.id)) += x
    q
  }

  def findTemplateByFlowName(flowName: String): Future[Seq[ChestnutTemplate]] = {
    val q = templateTable.templatePOs
      .filter(_.name === flowName)
      .filter(_.isDeleted === (False: Bool))
      .result
    dc.db.run(q)
  }
}
