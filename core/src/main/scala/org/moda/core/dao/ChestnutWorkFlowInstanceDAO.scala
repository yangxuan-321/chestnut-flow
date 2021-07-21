package org.moda.core.dao

import com.typesafe.scalalogging.Logger
import org.moda.common.database.DatabaseComponent
import org.moda.core.model.Tables
import org.moda.core.model.tables.ChestnutWorkFlowInstanceTable
import org.moda.idl.ChestnutWorkFlowInstance
import slick.sql.FixedSqlAction

/**
 * @author moda-matser
 * 2020/9/11 下午2:52
 */
object ChestnutWorkFlowInstanceDAO {
  def apply()(implicit dcc: DatabaseComponent): ChestnutWorkFlowInstanceDAO = new ChestnutWorkFlowInstanceDAO {
    override val dc: DatabaseComponent = dcc
  }
}

trait ChestnutWorkFlowInstanceDAO extends CoreDAO {
  import dc.profile.api._

  val logger: Logger = Logger(getClass)
  val workFlowInstanceTable: ChestnutWorkFlowInstanceTable = new Tables(dc)

  def insertWorkFlowInstance(x: ChestnutWorkFlowInstance): FixedSqlAction[Long, NoStream, Effect.Write] = {
    val q = (
      workFlowInstanceTable.chestnutWorkFlowInstancePOs
        returning workFlowInstanceTable.chestnutWorkFlowInstancePOs.map(_.id)
    ) += x
    q
  }
}
