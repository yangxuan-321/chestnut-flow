package org.moda.core.dao

import com.typesafe.scalalogging.Logger
import com.zz.cdp.common.util.TimeTransUtil
import org.moda.common.database.DatabaseComponent
import org.moda.core.model.Tables
import org.moda.core.model.tables.{ChestnutTemplateTable, ChestnutWorkFlowJsonTable}
import org.moda.idl.Bool.False
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
  val chestnutTemplateTable: ChestnutTemplateTable = new Tables(dc)

  def insertWorkFlowJsonTable(x: ChestnutWorkFlowJson): FixedSqlAction[Int, NoStream, Effect.Write] = {
    val q = workFlowJsonTable.workFlowJsonPOs += x
    // dc.db.run(q).map(_ > 0)
    q
  }

  def detailFlow(templateId: Long): Future[Seq[ChestnutWorkFlowJson]] = {
    val q = workFlowJsonTable.workFlowJsonPOs
      .join {
        chestnutTemplateTable.templatePOs.filter(_.isDeleted === (False: Bool))
      }
      .on(_.templateId === _.id)
      .sortBy(_._2.createdAt.desc)
      .map(_._1)
      .result
    dc.db.run(q)
  }
}
