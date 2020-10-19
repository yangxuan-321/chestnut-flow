package com.moda.core.dao

import com.zz.cdp.monitor.database.{HasDatabaseComponent, PgColumnMapping}
import com.zz.cdp.monitor.model.{ColumnTypesMapper, Tables}

trait DAO extends HasDatabaseComponent with ColumnTypesMapper with PgColumnMapping {

  protected val tables: Tables = Tables(dc)

}
