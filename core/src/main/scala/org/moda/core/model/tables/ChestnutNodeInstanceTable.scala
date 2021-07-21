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
trait ChestnutNodeInstanceTable {

  this: ColumnTypesMapper with PgColumnMapping =>
  import org.moda.common.database.DatabaseComponent.profile.api._

  val chestnutNodeInstancePOs: TableQuery[ChestnutNodeInstancePOs] =
    TableQuery[ChestnutNodeInstancePOs]((tag: Tag) => new ChestnutNodeInstancePOs(tag, "chestnut_flow_node_instance"))

  class ChestnutNodeInstancePOs(tag: Tag, tableName: String) extends Table[ChestnutNodeInstance](tag, tableName) {
    def id                                = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def nodeId: Rep[Long]                 = column[Long]("node_id", O.SqlType("BIGINT"), O.Default(0L))
    def flowInstanceId: Rep[Long]         = column[Long]("flow_instance_id", O.SqlType("BIGINT"), O.Default(0L))
    def status: Rep[NodeInstanceStatus]   = column[NodeInstanceStatus]("status", O.SqlType("SMALLINT"), O.Default(NodeInstanceStatus.NODE_INSTANCE_STATUS_NEW))
    def paramValue: Rep[String]           = column[String]("param_value", O.SqlType("TEXT"), O.Default(""))
    def createUser: Rep[Long]             = column[Long]("create_user", O.SqlType("BIGINT"), O.Default(0L))
    def updateUser: Rep[Long]             = column[Long]("update_user", O.SqlType("BIGINT"), O.Default(0L))
    def createdAt: Rep[Timestamp]         = column[Timestamp]("created_at", O.SqlType("timestamptz default now()"))
    def updatedAt: Rep[Timestamp]         = column[Timestamp]("updated_at", O.SqlType("timestamptz default now()"))

    def * = (
        id ::
        nodeId ::
        flowInstanceId::
        status::
        paramValue::
        createUser ::
        updateUser ::
        createdAt.mapToInstant ::
        updatedAt.mapToInstant ::
        HNil
      ).mapTo[ChestnutNodeInstance]
  }
}
