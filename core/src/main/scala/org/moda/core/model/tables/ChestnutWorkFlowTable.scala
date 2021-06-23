package org.moda.core.model.tables

import java.sql.Timestamp

import org.moda.common.database.PgColumnMapping
import org.moda.common.model.ColumnTypesMapper
import org.moda.idl.{ChestnutWorkFlow, FlowStatus}
import slick.collection.heterogeneous.HNil
/**
 * @author moda-matser
 * 2020/9/11 下午2:41
 */
trait ChestnutWorkFlowTable {

  this: ColumnTypesMapper with PgColumnMapping =>
  import org.moda.common.database.DatabaseComponent.profile.api._

  val chestnutWorkFlowPOs: TableQuery[ChestnutWorkFlowPOs] =
    TableQuery[ChestnutWorkFlowPOs]((tag: Tag) => new ChestnutWorkFlowPOs(tag, "chestnut_flow_workflow"))

  class ChestnutWorkFlowPOs(tag: Tag, tableName: String) extends Table[ChestnutWorkFlow](tag, tableName) {
    def id                              = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def flowName: Rep[String]           = column[String]("flow_name", O.SqlType("TEXT"), O.Default(""))
    def templateId: Rep[Long]           = column[Long]("template_id", O.SqlType("BIGINT"), O.Default(0L))
    def flowVersion: Rep[String]        = column[String]("flow_version", O.SqlType("TEXT"), O.Default(""))
    def version: Rep[Int]               = column[Int]("flow_version", O.SqlType("INT"), O.Default(0))
    def status: Rep[FlowStatus]         = column[FlowStatus]("status", O.SqlType("SMALLINT"), O.Default(FlowStatus.FLOW_STATUS_NORMAL))
    def description: Rep[String]        = column[String]("description", O.SqlType("TEXT"), O.Default(""))
    def createUser: Rep[Long]           = column[Long]("create_user", O.SqlType("BIGINT"), O.Default(0L))
    def updateUser: Rep[Long]           = column[Long]("update_user", O.SqlType("BIGINT"), O.Default(0L))
    def createdAt: Rep[Timestamp]       = column[Timestamp]("created_at", O.SqlType("timestamptz default now()"))
    def updatedAt: Rep[Timestamp]       = column[Timestamp]("updated_at", O.SqlType("timestamptz default now()"))

    def * = (
        id ::
        flowName ::
        templateId::
        flowVersion::
        version::
        status ::
        description::
        createUser ::
        updateUser ::
        createdAt.mapToInstant ::
        updatedAt.mapToInstant ::
        HNil
      ).mapTo[ChestnutWorkFlow]
  }
}
