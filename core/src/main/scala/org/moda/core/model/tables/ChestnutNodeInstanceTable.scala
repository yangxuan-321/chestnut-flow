package org.moda.core.model.tables

import java.sql.Timestamp

import org.moda.common.database.PgColumnMapping
import org.moda.common.model.ColumnTypesMapper
import org.moda.idl._
import slick.collection.heterogeneous.HNil
import slick.lifted.MappedToBase.mappedToIsomorphism

/**
 * @author moda-matser
 * 2020/9/11 下午2:41
 */
trait ChestnutNodeInstanceTable {

  this: ColumnTypesMapper with PgColumnMapping =>
  import org.moda.common.database.DatabaseComponent.profile.api._

  val chestnutNodePOs: TableQuery[ChestnutNodePOs] =
    TableQuery[ChestnutNodePOs]((tag: Tag) => new ChestnutNodePOs(tag, "chestnut_flow_node"))

  class ChestnutNodePOs(tag: Tag, tableName: String) extends Table[ChestnutNode](tag, tableName) {
    def id                                = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def nodeId: Rep[Long]                 = column[Long]("node_id", O.SqlType("BIGINT"), O.Default(0L))
    def flowInstanceId: Rep[Long]         = column[Long]("flow_instance_id", O.SqlType("BIGINT"), O.Default(0L))
    def status: Rep[NodeInstanceStatus]   = column[NodeInstanceStatus]("status", O.SqlType("SMALLINT"), O.Default(NodeInstanceStatus.NODE_INSTANCE_STATUS_NEW))
    def nodeName: Rep[String]           = column[String]("node_name", O.SqlType("TEXT"), O.Default(""))
    def nodeType: Rep[FlowNodeType]     = column[FlowNodeType]("node_type", O.SqlType("SMALLINT"), O.Default(FlowNodeType.FLOW_NODE_TYPE_UNKNOWN))
    def icon: Rep[String]               = column[String]("icon", O.SqlType("TEXT"), O.Default(""))
    def nodeUuid: Rep[String]           = column[String]("node_uuid", O.SqlType("TEXT"), O.Default(""))
    def nodeVersion: Rep[Int]           = column[Int]("node_version", O.SqlType("INT"), O.Default(0))
    def description: Rep[String]        = column[String]("description", O.SqlType("TEXT"), O.Default(""))
    def flowId: Rep[Long]               = column[Long]("flow_id", O.SqlType("BIGINT"), O.Default(0L))
    def isDeleted: Rep[Bool]            = column[Bool]("is_deleted", O.SqlType("SMALLINT"), O.Default(Bool.False))
    def createUser: Rep[Long]           = column[Long]("create_user", O.SqlType("BIGINT"), O.Default(0L))
    def updateUser: Rep[Long]           = column[Long]("update_user", O.SqlType("BIGINT"), O.Default(0L))
    def createdAt: Rep[Timestamp]       = column[Timestamp]("created_at", O.SqlType("timestamptz default now()"))
    def updatedAt: Rep[Timestamp]       = column[Timestamp]("updated_at", O.SqlType("timestamptz default now()"))

    def * = (
        id ::
        nodeName ::
        nodeType::
        icon::
        nodeUuid::
        nodeVersion ::
        description::
        flowId::
        isDeleted::
        createUser ::
        updateUser ::
        createdAt.mapToInstant ::
        updatedAt.mapToInstant ::
        HNil
      ).mapTo[ChestnutNode]
  }
}
