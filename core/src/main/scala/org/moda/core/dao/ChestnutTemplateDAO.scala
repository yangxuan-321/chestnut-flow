package org.moda.core.dao

import com.typesafe.scalalogging.Logger
import com.zz.cdp.common.util.TimeTransUtil
import org.moda.common.database.DatabaseComponent
import org.moda.core.model.Tables
import org.moda.core.model.tables.{ChestnutTemplateTable, ChestnutWorkFlowTable}
import org.moda.idl.Bool.False
import org.moda.idl._
import slick.sql.{FixedSqlAction, SqlAction}

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
  val workFlowTable: ChestnutWorkFlowTable = new Tables(dc)

  def insertTemplate(x: ChestnutTemplate): FixedSqlAction[Long, NoStream, Effect.Write] = {
    val q = (templateTable.templatePOs returning templateTable.templatePOs.map(_.id)) += x
    q
  }

  def findTemplateByFlowName(flowName: String): Future[Option[ChestnutTemplate]] = {
    val q = templateTable.templatePOs
      .filter(_.name === flowName)
      .filter(_.isDeleted === (False: Bool))
      .take(1)
      .result
    dc.db.run(q).map(_.headOption)
  }

  def findTemplateByFlowName1(flowName: String): SqlAction[Option[ChestnutTemplate], NoStream, Effect.Read] = {
    val q = templateTable.templatePOs
      .filter(_.name === flowName)
      .filter(_.isDeleted === (False: Bool))
      .take(1)
      .result
      .headOption
    q
  }

  def listFlowTemplate(req: FlowManagerListReq): Future[Seq[ChestnutWorkFlow]] = {
    val startDate = req.startDate.map(TimeTransUtil.stringDateToLong)
    // 向后推算一天
    val endDate = req.endDate.map(TimeTransUtil.stringDateToLong).map(_ + 1000*3600*24)
    val q = templateTable.templatePOs
      .maybeFilter(req.flowName)((r, o) => r.name.like(s"%$o%"))
      .maybeFilter(startDate)((r, o) => r.createdAt >= (o: Long).asTimestamp)
      .maybeFilter(endDate)((r, o) => r.createdAt < (o: Long).asTimestamp)
      .filter(_.isDeleted === (False: Bool))
      .join(
        // workFlowTable.chestnutWorkFlowPOs.filter(_.status === (FLOW_STATUS_NORMAL: FlowStatus))
        workFlowTable.chestnutWorkFlowPOs
      )
      .on(_.id === _.templateId)
      .sortBy(_._1.createdAt)
      .map(_._2)
      .result
    dc.db.run(q)
  }
}
