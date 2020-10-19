package org.moda.core.dao

import org.moda.core.database.{HasDatabaseComponent, PgColumnMapping}
import org.moda.core.model.{ColumnTypesMapper, Tables}

trait DAO extends HasDatabaseComponent with ColumnTypesMapper with PgColumnMapping {

  protected val tables: Tables = Tables(dc)

}
