package org.moda.core.model.tables

import java.sql.Timestamp

import org.moda.core.database.PgColumnMapping
import org.moda.core.model.ColumnTypesMapper
import org.moda.idl.{DataStatus, TestUserPO}
import slick.collection.heterogeneous.HNil

/**
 * @author huanggh
 * 2020/9/11 下午2:41
 */
trait TestUserTable {

  this: ColumnTypesMapper with PgColumnMapping =>
  import org.moda.core.database.DatabaseComponent.profile.api._

  val testUserPOs: TableQuery[TestUserPOs] =
    TableQuery[TestUserPOs]((tag: Tag) => new TestUserPOs(tag, "test_user"))

  class TestUserPOs(tag: Tag, tableName: String) extends Table[TestUserPO](tag, tableName) {
    def id                             = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def username: Rep[String]          = column[String]("username", O.SqlType("TEXT"), O.Default(""))
    def password: Rep[String]          = column[String]("password", O.SqlType("TEXT"), O.Default(""))
    def status: Rep[DataStatus]        = column[DataStatus]("status", O.SqlType("SMALLINT"), O.Default(DataStatus.Normal))
    def createdAt: Rep[Timestamp]      = column[Timestamp]("created_at", O.SqlType("timestamptz default now()"))
    def updatedAt: Rep[Timestamp]      = column[Timestamp]("updated_at", O.SqlType("timestamptz default now()"))

    def * = (
        id ::
        username ::
        password ::
        status ::
        createdAt.mapToInstant ::
        updatedAt.mapToInstant ::
        HNil
      ).mapTo[TestUserPO]
  }
}
