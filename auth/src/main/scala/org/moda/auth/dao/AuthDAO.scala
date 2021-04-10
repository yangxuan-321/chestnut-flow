package org.moda.auth.dao

import org.moda.common.database.{HasDatabaseComponent, PgColumnMapping}
import org.moda.common.model.ColumnTypesMapper
import org.moda.core.model.Tables

trait AuthDAO extends HasDatabaseComponent with ColumnTypesMapper with PgColumnMapping {

  protected val tables: Tables = Tables(dc)

}
