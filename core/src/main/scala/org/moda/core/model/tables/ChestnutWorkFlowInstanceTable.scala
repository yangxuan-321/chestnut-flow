package org.moda.core.model.tables

import java.sql.Timestamp

import org.moda.common.database.PgColumnMapping
import org.moda.common.model.ColumnTypesMapper
import org.moda.idl.{ChestnutWorkFlow, ChestnutWorkFlowInstance, FlowInstanceStatus, FlowStatus}
import slick.collection.heterogeneous.HNil

/**
 * @author moda-matser
 * 2020/9/11 下午2:41
 */
trait ChestnutWorkFlowInstanceTable {

  this: ColumnTypesMapper with PgColumnMapping =>
  import org.moda.common.database.DatabaseComponent.profile.api._

  val chestnutWorkFlowInstancePOs: TableQuery[ChestnutWorkFlowInstancePOs] =
    TableQuery[ChestnutWorkFlowInstancePOs]((tag: Tag) => new ChestnutWorkFlowInstancePOs(tag, "chestnut_flow_workflow_instance"))

  class ChestnutWorkFlowInstancePOs(tag: Tag, tableName: String) extends Table[ChestnutWorkFlowInstance](tag, tableName) {
    def id                              = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def flowId: Rep[Long]               = column[Long]("flow_id", O.SqlType("BIGINT"), O.Default(0L))
    def status: Rep[FlowInstanceStatus] = column[FlowInstanceStatus]("status", O.SqlType("SMALLINT"), O.Default(FlowInstanceStatus.FLOW_INSTANCE_STATUS_NEW))
    def paramValue: Rep[String]         = column[String]("param_value", O.SqlType("TEXT"), O.Default(""))
    def createUser: Rep[Long]           = column[Long]("create_user", O.SqlType("BIGINT"), O.Default(0L))
    def updateUser: Rep[Long]           = column[Long]("update_user", O.SqlType("BIGINT"), O.Default(0L))
    def createdAt: Rep[Timestamp]       = column[Timestamp]("created_at", O.SqlType("timestamptz default now()"))
    def updatedAt: Rep[Timestamp]       = column[Timestamp]("updated_at", O.SqlType("timestamptz default now()"))

    def * = (
        id ::
        flowId ::
        status::
        paramValue::
        createUser ::
        updateUser ::
        createdAt.mapToInstant ::
        updatedAt.mapToInstant ::
        HNil
      ).mapTo[ChestnutWorkFlowInstance]
  }
}
