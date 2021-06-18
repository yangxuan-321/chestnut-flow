package org.moda.core.dao

import com.typesafe.scalalogging.Logger
import org.moda.common.database.DatabaseComponent
import org.moda.core.model.Tables
import org.moda.core.model.tables.ChestnutWorkFlowJsonTable
import org.moda.idl._

import scala.concurrent.Future

/**
 * @author moda-matser
 * 2020/9/11 下午2:52
 */
object ChestnutWorkFlowJsonDAO {
  def apply()(implicit dcc: DatabaseComponent): ChestnutWorkFlowJsonDAO = new ChestnutWorkFlowJsonDAO {
    override val dc: DatabaseComponent = dcc
  }
}

trait ChestnutWorkFlowJsonDAO extends CoreDAO {
  import dc.profile.api._

  import scala.concurrent.ExecutionContext.Implicits.global

  val logger: Logger = Logger(getClass)
  val workFlowJsonTable: ChestnutWorkFlowJsonTable = new Tables(dc)

  def insertWorkFlowJsonTable(x: ChestnutWorkFlowJson): Future[Boolean] = {
    val q = workFlowJsonTable.workFlowJsonPOs += x
    dc.db.run(q).map(_ > 0)
  }
}
