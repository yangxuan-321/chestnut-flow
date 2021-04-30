package org.moda.auth.dao

import org.moda.common.dao.DAO
import org.moda.core.model.Tables

trait AuthDAO extends DAO {

  protected val tables: Tables = Tables(dc)

}
