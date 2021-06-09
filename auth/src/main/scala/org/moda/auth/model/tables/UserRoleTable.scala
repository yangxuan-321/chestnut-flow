package org.moda.auth.model.tables

import org.moda.common.database.PgColumnMapping
import org.moda.common.model.ColumnTypesMapper
import org.moda.idl.{AuthUser, RoleType, UserRole}
import slick.collection.heterogeneous.HNil

/**
 * @author moda-matser
 * 2020/9/11 下午2:41
 */
trait UserRoleTable {

  this: ColumnTypesMapper with PgColumnMapping =>
  import org.moda.common.database.DatabaseComponent.profile.api._

  val userRolePOs: TableQuery[UserInfoPOs] =
    TableQuery[UserInfoPOs]((tag: Tag) => new UserInfoPOs(tag, "user_role"))

  class UserInfoPOs(tag: Tag, tableName: String) extends Table[UserRole](tag, tableName) {
    def userId: Rep[Long]              = column[Long]("user_id", O.SqlType("BIGINT"), O.Default(0))
    def roleType: Rep[RoleType]        = column[RoleType]("user_role", O.SqlType("SMALLINT"), O.Default(RoleType.READ))

    def * = (
        userId ::
        roleType ::
        HNil
      ).mapTo[UserRole]
  }
}
