package org.moda.core.model.tables

import java.sql.Timestamp

import org.moda.core.database.{DatabaseComponent, PgColumnMapping}
import org.moda.core.model.ColumnTypesMapper
import org.moda.idl.{AuthUser, Bool}
import slick.collection.heterogeneous.HNil
/**
 * @author moda-matser
 * 2020/9/11 下午2:41
 */
trait AuthUserTable {

  this: ColumnTypesMapper with PgColumnMapping =>
  import DatabaseComponent.profile.api._

  val authUserPOs: TableQuery[AuthUserPOs] =
    TableQuery[AuthUserPOs]((tag: Tag) => new AuthUserPOs(tag, "auth_user"))

  class AuthUserPOs(tag: Tag, tableName: String) extends Table[AuthUser](tag, tableName) {
    def id                             = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def username: Rep[String]          = column[String]("username", O.SqlType("TEXT"), O.Default(""))
    def email: Rep[String]             = column[String]("email", O.SqlType("TEXT"), O.Default(""))
    def password: Rep[String]          = column[String]("password", O.SqlType("TEXT"), O.Default(""))
    def isDelete:   Rep[Bool]          = column[Bool]("is_delete", O.SqlType("SMALLINT"), O.Default(Bool.False))
    def createdAt: Rep[Timestamp]      = column[Timestamp]("created_at", O.SqlType("timestamptz default now()"))
    def updatedAt: Rep[Timestamp]      = column[Timestamp]("updated_at", O.SqlType("timestamptz default now()"))

    def * = (
        id ::
        username ::
        email ::
        password ::
        isDelete ::
        createdAt.mapToInstant ::
        updatedAt.mapToInstant ::
        HNil
      ).mapTo[AuthUser]
  }
}
