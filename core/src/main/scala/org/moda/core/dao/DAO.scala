package org.moda.core.dao

import com.moda.core.model.Tables
import com.zz.cdp.monitor.database.PgColumnMapping
import org.moda.core.database.{HasDatabaseComponent, PgColumnMapping}
import org.moda.core.model.{ColumnTypesMapper, Tables}

trait DAO extends HasDatabaseComponent with ColumnTypesMapper with PgColumnMapping {

  protected val tables: Tables = Tables(dc)

}
