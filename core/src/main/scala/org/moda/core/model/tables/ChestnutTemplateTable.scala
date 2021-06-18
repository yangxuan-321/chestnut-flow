package org.moda.core.model.tables

import java.sql.Timestamp

import org.moda.common.database.PgColumnMapping
import org.moda.common.model.ColumnTypesMapper
import org.moda.idl.{AuthUser, Bool}
import slick.collection.heterogeneous.HNil
/**
 * @author moda-matser
 * 2020/9/11 下午2:41
 */
trait ChestnutTemplateTable {

  this: ColumnTypesMapper with PgColumnMapping =>
  import org.moda.common.database.DatabaseComponent.profile.api._

  val authUserPOs: TableQuery[ChestnutTemplatePOs] =
    TableQuery[ChestnutTemplatePOs]((tag: Tag) => new ChestnutTemplatePOs(tag, "chestnut_flow_template"))

  class ChestnutTemplatePOs(tag: Tag, tableName: String) extends Table[AuthUser](tag, tableName) {
    def id                              = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name: Rep[String]               = column[String]("name", O.SqlType("TEXT"), O.Default(""))
    def isDeleted: Rep[Bool]            = column[String]("is_deleted", O.SqlType("TEXT"), O.Default(""))
    def createdAt: Rep[Timestamp]       = column[Timestamp]("created_at", O.SqlType("timestamptz default now()"))
    def updatedAt: Rep[Timestamp]       = column[Timestamp]("updated_at", O.SqlType("timestamptz default now()"))
    def createdAt: Rep[Timestamp]       = column[Timestamp]("created_at", O.SqlType("timestamptz default now()"))
    def updatedAt: Rep[Timestamp]       = column[Timestamp]("updated_at", O.SqlType("timestamptz default now()"))

    def * = (
        id ::
        username ::
        email ::
        password ::
        nickname ::
        avatar ::
        isDelete ::
        createdAt.mapToInstant ::
        updatedAt.mapToInstant ::
        HNil
      ).mapTo[AuthUser]
  }
}
