package org.moda.core.model

import org.moda.core.database.{DatabaseComponent, HasDatabaseComponent, PgColumnMapping}
import org.moda.core.model.tables.TestUserTable

object Tables {

  def apply(dc: DatabaseComponent): Tables = new Tables(dc)

}

class Tables(val dc: DatabaseComponent)
  extends HasDatabaseComponent
    with ColumnTypesMapper
    with PgColumnMapping
    with TestUserTable
//    with PlatformAdvertiserInfoTable
