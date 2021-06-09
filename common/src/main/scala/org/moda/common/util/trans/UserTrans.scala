package org.moda.common.util.trans

import org.moda.idl.RoleType

object UserTrans {
  def roleType2Str(r: RoleType): String = {
    lazy val ms: Map[RoleType, String] = Map(
      RoleType.ADMIN        ->  "admin",
      RoleType.READ_WRITE   ->  "read_write",
      RoleType.READ         ->  "read"
    )

    ms.getOrElse(r, "read")
  }
}
