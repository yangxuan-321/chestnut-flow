package org.moda.core.model.tables

import java.sql.Timestamp

import org.moda.common.database.PgColumnMapping
import org.moda.common.model.ColumnTypesMapper
import org.moda.idl._
import slick.collection.heterogeneous.HNil

/**
 * @author moda-matser
 * 2020/9/11 下午2:41
 */
trait ChestnutNodeRouterInstanceTable {

  this: ColumnTypesMapper with PgColumnMapping =>
  import org.moda.common.database.DatabaseComponent.profile.api._

  val chestnutNodeRouterInstancePOs: TableQuery[ChestnutNodeRouterInstancePOs] =
    TableQuery[ChestnutNodeRouterInstancePOs]((tag: Tag) => new ChestnutNodeRouterInstancePOs(tag, "chestnut_flow_node_router_instance"))

  class ChestnutNodeRouterInstancePOs(tag: Tag, tableName: String) extends Table[ChestnutNodeRouterInstance](tag, tableName) {
    def id                              = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def nodeRouterId                    = column[Long]("node_router_id", O.SqlType("BIGINT"), O.Default(0L))
    def flowInstanceId: Rep[Long]       = column[Long]("flow_instance_id", O.SqlType("BIGINT"), O.Default(0L))
    def sourceNodeInstanceId: Rep[Long] = column[Long]("source_node_instance_id", O.SqlType("BIGINT"), O.Default(0L))
    def targetNodeInstanceId: Rep[Long] = column[Long]("target_node_instance_id", O.SqlType("BIGINT"), O.Default(0L))
    def createUser: Rep[Long]           = column[Long]("create_user", O.SqlType("BIGINT"), O.Default(0L))
    def updateUser: Rep[Long]           = column[Long]("update_user", O.SqlType("BIGINT"), O.Default(0L))
    def createdAt: Rep[Timestamp]       = column[Timestamp]("created_at", O.SqlType("timestamptz default now()"))
    def updatedAt: Rep[Timestamp]       = column[Timestamp]("updated_at", O.SqlType("timestamptz default now()"))

    def * = (
        id ::
        nodeRouterId ::
        flowInstanceId::
        sourceNodeInstanceId::
        targetNodeInstanceId::
        createUser ::
        updateUser ::
        createdAt.mapToInstant ::
        updatedAt.mapToInstant ::
        HNil
      ).mapTo[ChestnutNodeRouterInstance]
  }
}
