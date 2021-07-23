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
object ChestnutNodeRouterDAO {
  def apply()(implicit dcc: DatabaseComponent): ChestnutNodeRouterDAO = new ChestnutNodeRouterDAO {
    override val dc: DatabaseComponent = dcc
  }
}

trait ChestnutNodeRouterDAO extends CoreDAO {
  import dc.profile.api._

  import scala.concurrent.ExecutionContext.Implicits.global

  val logger: Logger = Logger(getClass)
  val chestnutNodeRouterTable: ChestnutNodeRouterTable = new Tables(dc)

  def insertFlowNodeRouters(x: List[ChestnutNodeRouter]): FixedSqlAction[Seq[ChestnutNodeRouter], NoStream, Effect.Write] = {
    val q = (chestnutNodeRouterTable.chestnutNodeRouterPOs returning chestnutNodeRouterTable.chestnutNodeRouterPOs) ++= x
    q
  }

  def deleteNodeRouterByFlowId(id: Long): FixedSqlAction[Int, NoStream, Effect.Write] = {
    chestnutNodeRouterTable.chestnutNodeRouterPOs.filter(_.flowId === id).delete
  }
}
