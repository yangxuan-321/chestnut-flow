package org.moda.core.dao

import com.typesafe.scalalogging.Logger
import org.moda.common.database.DatabaseComponent
import org.moda.core.model.Tables
import org.moda.core.model.tables.{ChestnutWorkFlowJsonTable, ChestnutWorkFlowTable}
import org.moda.idl._
import slick.sql.FixedSqlAction

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
  val chestnutWorkFlowTable: ChestnutWorkFlowTable = new Tables(dc)

  def insertWorkFlowJsonTable(x: ChestnutWorkFlowJson): FixedSqlAction[Int, NoStream, Effect.Write] = {
    val q = workFlowJsonTable.workFlowJsonPOs += x
    // dc.db.run(q).map(_ > 0)
    q
  }

  def detailFlow(templateId: Long, flowVersion: String): Future[Option[ChestnutWorkFlowJson]] = {
    val q = workFlowJsonTable.workFlowJsonPOs
      .filter( x => x.templateId === templateId && x.flowVersion === flowVersion)
      .take(1)
      .result
    dc.db.run(q).map(_.headOption)
  }
}
