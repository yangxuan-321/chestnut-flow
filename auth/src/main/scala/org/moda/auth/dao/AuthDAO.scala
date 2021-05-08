package org.moda.auth.dao

import org.moda.auth.model.Tables
import org.moda.common.dao.DAO

trait AuthDAO extends DAO {

  protected val tables: Tables = Tables(dc)

}
