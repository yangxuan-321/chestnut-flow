package org.moda.core.dao

import com.typesafe.scalalogging.Logger
import org.moda.common.database.DatabaseComponent
import org.moda.core.model.Tables
import org.moda.core.model.tables.{ChestnutNodeInstanceTable, ChestnutNodeRouterTable, ChestnutNodeTable}
import org.moda.idl._
import slick.sql.FixedSqlAction
import slick.jdbc.GetResult

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

  implicit val nodeInstanceResult: AnyRef with GetResult[ChestnutNodeInstance] = GetResult(
    r => ChestnutNodeInstance(
      r.nextLong(),
      r.nextLong(),
      r.nextLong(),
      NodeInstanceStatus.fromValue(r.nextInt()),
      r.nextString(),
      r.nextInt(),
      r.nextLong(),
      r.nextLong(),
      r.nextTimestamp().toInstant,
      r.nextTimestamp().toInstant
    )
  )

  val logger: Logger = Logger(getClass)
  val chestnutNodeInstanceTable: ChestnutNodeInstanceTable = new Tables(dc)
  val chestnutNodeTable: ChestnutNodeTable = new Tables(dc)

  def insertFlowNodeInstances(x: List[ChestnutNodeInstance]): FixedSqlAction[Seq[ChestnutNodeInstance], NoStream, Effect.Write] = {
    val q = (chestnutNodeInstanceTable.chestnutNodeInstancePOs returning chestnutNodeInstanceTable.chestnutNodeInstancePOs) ++= x
    q
  }

  def queryTaskInstanceInfo(nodeTypes: Vector[FlowNodeType], size: Int = 50): Future[Vector[ChestnutNodeInstance]] = {
    val nodeTypesStr = nodeTypes.map(_.value).mkString(", ")
    val sql = sql"""
      with x as (
        select id, version from chestnut_flow_node_instance
        where status = ${NodeInstanceStatus.NODE_INSTANCE_STATUS_NEW.value}
        node_id in (
          select id from chestnut_flow_node where node_type in ($nodeTypesStr)
        )
        order by updated_at asc limit $size
        for update
      )
      update chestnut_flow_node_instance t
      set
        status = ${NodeInstanceStatus.NODE_INSTANCE_STATUS_OBTAIN.value}
        version = t.version + 1,
        updated_at = now()
      from x
      where t.id = x.id and t.version = x.version
      returning t.id, t.node_id, t.flow_instance_id, t.status, t.param_value,
      t.version, t.create_user, t.update_user, t.created_at, t.updated_at
    """.as[ChestnutNodeInstance]

    dc.db.run(sql)
  }
}
