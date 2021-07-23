package org.moda.core.dao

import com.typesafe.scalalogging.Logger
import org.moda.common.database.DatabaseComponent
import org.moda.core.model.Tables
import org.moda.core.model.tables.{ChestnutNodeRouterTable, ChestnutNodeTable}
import org.moda.idl._
import slick.sql.FixedSqlAction

import scala.concurrent.Future

/**
 * @author moda-matser
 * 2020/9/11 下午2:52
 */
object ChestnutNodeDAO {
  def apply()(implicit dcc: DatabaseComponent): ChestnutNodeDAO = new ChestnutNodeDAO {
    override val dc: DatabaseComponent = dcc
  }
}

trait ChestnutNodeDAO extends CoreDAO {

  import dc.profile.api._

  import scala.concurrent.ExecutionContext.Implicits.global

  val logger: Logger = Logger(getClass)
  val chestnutNodeTable: ChestnutNodeTable = new Tables(dc)
  val chestnutNodeRouterTable: ChestnutNodeRouterTable = new Tables(dc)

  def insertFlowNodes(x: List[ChestnutNode]): FixedSqlAction[Seq[ChestnutNode], NoStream, Effect.Write] = {
    val q = (chestnutNodeTable.chestnutNodePOs returning chestnutNodeTable.chestnutNodePOs) ++= x
    q
  }

  def queryStartNodeByFlowId(flowId: Long): Future[Option[ChestnutNode]] = {
    val q = chestnutNodeTable.chestnutNodePOs
      .filter(_.flowId === flowId)
      .filter(_.nodeType === (FlowNodeType.FLOW_NODE_TYPE_START: FlowNodeType))
      .take(1)
      .result
    dc.db.run(q).map(_.headOption)
  }

  def deleteNodeByFlowId(flowId: Long): FixedSqlAction[Int, NoStream, Effect.Write] = {
    chestnutNodeTable.chestnutNodePOs
      .filter(_.flowId === flowId)
      .delete
  }
}
