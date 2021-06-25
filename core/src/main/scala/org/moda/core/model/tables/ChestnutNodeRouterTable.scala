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
trait ChestnutNodeRouterTable {

  this: ColumnTypesMapper with PgColumnMapping =>
  import org.moda.common.database.DatabaseComponent.profile.api._

  val chestnutNodeRouterPOs: TableQuery[ChestnutNodeRouterPOs] =
    TableQuery[ChestnutNodeRouterPOs]((tag: Tag) => new ChestnutNodeRouterPOs(tag, "chestnut_flow_node_router"))

  class ChestnutNodeRouterPOs(tag: Tag, tableName: String) extends Table[ChestnutNodeRouter](tag, tableName) {
    def id                              = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def flowId: Rep[Long]               = column[Long]("flow_id", O.SqlType("BIGINT"), O.Default(0L))
    def sourceNodeId: Rep[Long]         = column[Long]("source_node_id", O.SqlType("BIGINT"), O.Default(0L))
    def targetNodeId: Rep[Long]         = column[Long]("target_node_id", O.SqlType("BIGINT"), O.Default(0L))
    def routerRule: Rep[String]         = column[String]("router_rule", O.SqlType("TEXT"), O.Default(""))
    def description: Rep[String]        = column[String]("description", O.SqlType("TEXT"), O.Default(""))
    def isDeleted: Rep[Bool]            = column[Bool]("is_deleted", O.SqlType("SMALLINT"), O.Default(Bool.False))
    def createUser: Rep[Long]           = column[Long]("create_user", O.SqlType("BIGINT"), O.Default(0L))
    def updateUser: Rep[Long]           = column[Long]("update_user", O.SqlType("BIGINT"), O.Default(0L))
    def createdAt: Rep[Timestamp]       = column[Timestamp]("created_at", O.SqlType("timestamptz default now()"))
    def updatedAt: Rep[Timestamp]       = column[Timestamp]("updated_at", O.SqlType("timestamptz default now()"))

    def * = (
        id ::
        flowId ::
        sourceNodeId::
        targetNodeId::
        routerRule::
        description::
        isDeleted::
        createUser ::
        updateUser ::
        createdAt.mapToInstant ::
        updatedAt.mapToInstant ::
        HNil
      ).mapTo[ChestnutNodeRouter]
  }
}
