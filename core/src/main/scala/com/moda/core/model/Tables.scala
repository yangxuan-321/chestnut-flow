package com.moda.core.model

import com.zz.cdp.monitor.database.{DatabaseComponent, HasDatabaseComponent, PgColumnMapping}
import com.zz.cdp.monitor.model.tables.{ChannelAuthCodeTable, FooTable, OceanengineSyncTaskTable, PlatformAdvertiserInfoTable}

object Tables {

  def apply(dc: DatabaseComponent): Tables = new Tables(dc)

}

class Tables(val dc: DatabaseComponent)
  extends HasDatabaseComponent
    with ColumnTypesMapper
    with PgColumnMapping
    with ChannelAuthCodeTable
    with PlatformAdvertiserInfoTable
    with OceanengineSyncTaskTable
    with FooTable
