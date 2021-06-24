package org.moda.core.dao

import com.typesafe.scalalogging.Logger
import org.moda.common.database.DatabaseComponent
import org.moda.core.model.Tables
import org.moda.core.model.tables.ChestnutWorkFlowTable
import org.moda.idl.FlowStatus._
import org.moda.idl.{ChestnutWorkFlow, FlowStatus}
import slick.sql.FixedSqlAction

import scala.concurrent.Future

/**
 * @author moda-matser
 * 2020/9/11 下午2:52
 */
object ChestnutWorkFlowDAO {
  def apply()(implicit dcc: DatabaseComponent): ChestnutWorkFlowDAO = new ChestnutWorkFlowDAO {
    override val dc: DatabaseComponent = dcc
  }
}

trait ChestnutWorkFlowDAO extends CoreDAO {
  import dc.profile.api._

  import scala.concurrent.ExecutionContext.Implicits.global

  val logger: Logger = Logger(getClass)
  val workFlowTable: ChestnutWorkFlowTable = new Tables(dc)

  def insertWorkFlow(x: ChestnutWorkFlow): FixedSqlAction[Long, NoStream, Effect.Write] = {
    val q = (workFlowTable.chestnutWorkFlowPOs returning workFlowTable.chestnutWorkFlowPOs.map(_.id)) += x
    q
  }

  def findByFlowNameAndVersion(flowName: String, flowVersion: String): Future[Option[ChestnutWorkFlow]] = {
    val q = workFlowTable.chestnutWorkFlowPOs
      .filter(_.flowName === flowName)
      .filter(_.flowVersion === flowVersion)
      .filter(_.status === (FLOW_STATUS_NORMAL: FlowStatus))
      .take(1)
      .result
    dc.db.run(q.map(_.headOption))
  }
}
