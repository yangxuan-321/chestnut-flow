package org.moda.core.model.tables

import org.moda.common.database.PgColumnMapping
import org.moda.common.model.ColumnTypesMapper
import org.moda.idl.ChestnutWorkFlowJson
import slick.collection.heterogeneous.HNil
/**
 * @author moda-matser
 * 2020/9/11 下午2:41
 */
trait ChestnutWorkFlowJsonTable {

  this: ColumnTypesMapper with PgColumnMapping =>
  import org.moda.common.database.DatabaseComponent.profile.api._

  val workFlowJsonPOs: TableQuery[ChestnutWorkFlowJsonPOs] =
    TableQuery[ChestnutWorkFlowJsonPOs]((tag: Tag) => new ChestnutWorkFlowJsonPOs(tag, "chestnut_flow_workflow_json"))

  class ChestnutWorkFlowJsonPOs(tag: Tag, tableName: String) extends Table[ChestnutWorkFlowJson](tag, tableName) {
    def templateId: Rep[Long]         = column[Long]("template_id", O.SqlType("BIGINT"), O.Default(0L))
    def flowVersion: Rep[String]        = column[String]("flow_version", O.SqlType("TEXT"), O.Default(""))
    def flowData: Rep[String]           = column[String]("flow_data", O.SqlType("TEXT"), O.Default(""))

    def * = (
      templateId ::
      flowVersion ::
      flowData ::
      HNil
    ).mapTo[ChestnutWorkFlowJson]
  }
}
