package com.moda.core.model.tables

import com.zz.cdp.monitor.database.{DatabaseComponent, HasDatabaseComponent, PgColumnMapping}
import com.zz.cdp.monitor.model.{ColumnTypesMapper, Foo}
import slick.collection.heterogeneous.HNil
import slick.lifted.ProvenShape

/**
 * @author huanggh
 * 2020/9/11 下午2:41
 */
trait FooTable {

//  this: HasDatabaseComponent with ColumnTypesMapper with PgColumnMapping =>
//  import dc.profile.api._
  import DatabaseComponent.profile.api._

  class Foos(tag: Tag, tableName: String) extends Table[Foo](tag, tableName) {
    def id: Rep[Long]     = column[Long]("id", O.PrimaryKey)
    def name: Rep[String] = column[String]("name")
    def * : ProvenShape[Foo] = (id, name).mapTo[Foo]
  }

  val foos: TableQuery[Foos] = TableQuery[Foos]((t: Tag) => new Foos(t, "foo"))
}
