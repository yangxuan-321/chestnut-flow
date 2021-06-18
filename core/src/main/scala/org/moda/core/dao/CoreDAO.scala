package org.moda.core.dao

import org.moda.auth.model.Tables
import org.moda.common.dao.DAO

trait CoreDAO extends DAO {

  protected val tables: Tables = Tables(dc)

}
