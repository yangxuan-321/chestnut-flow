package org.moda.core.model

import org.moda.common.database.{DatabaseComponent, HasDatabaseComponent, PgColumnMapping}
import org.moda.common.model.ColumnTypesMapper
import org.moda.core.model.tables.{ChestnutNodeInstanceTable, ChestnutNodeRouterTable, ChestnutNodeTable, ChestnutTemplateTable, ChestnutWorkFlowInstanceTable, ChestnutWorkFlowJsonTable, ChestnutWorkFlowTable}

object Tables {

  def apply(dc: DatabaseComponent): Tables = new Tables(dc)

}

class Tables(val dc: DatabaseComponent)
  extends HasDatabaseComponent
    with ColumnTypesMapper
    with PgColumnMapping
    with ChestnutWorkFlowJsonTable
    with ChestnutTemplateTable
    with ChestnutWorkFlowTable
    with ChestnutNodeTable
    with ChestnutNodeRouterTable
    with ChestnutNodeInstanceTable
    with ChestnutWorkFlowInstanceTable
