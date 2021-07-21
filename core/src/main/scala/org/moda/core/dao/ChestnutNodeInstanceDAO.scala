package org.moda.core.dao

import com.typesafe.scalalogging.Logger
import org.moda.common.database.DatabaseComponent
import org.moda.core.model.Tables
import org.moda.core.model.tables.{ChestnutNodeInstanceTable, ChestnutNodeRouterTable, ChestnutNodeTable}
import org.moda.idl._
import slick.sql.FixedSqlAction

import scala.concurrent.Future

/**
 * @author moda-matser
 * 2020/9/11 下午2:52
 */
object ChestnutNodeInstanceDAO {
  def apply()(implicit dcc: DatabaseComponent): ChestnutNodeInstanceDAO = new ChestnutNodeInstanceDAO {
    override val dc: DatabaseComponent = dcc
  }
}

trait ChestnutNodeInstanceDAO extends CoreDAO {

  import dc.profile.api._

  import scala.concurrent.ExecutionContext.Implicits.global

  val logger: Logger = Logger(getClass)
  val chestnutNodeInstanceTable: ChestnutNodeInstanceTable = new Tables(dc)

  def insertFlowNodeInstances(x: List[ChestnutNodeInstance]): FixedSqlAction[Seq[ChestnutNodeInstance], NoStream, Effect.Write] = {
    val q = (chestnutNodeInstanceTable.chestnutNodeInstancePOs returning chestnutNodeInstanceTable.chestnutNodeInstancePOs) ++= x
    q
  }
}
