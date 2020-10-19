package org.moda.core.database

import slick.jdbc.JdbcBackend.Database
import slick.jdbc.{JdbcProfile, PostgresProfile}

object DatabaseComponent extends DatabaseComponent {

  //  def apply(): DatabaseComponent = new DatabaseComponent {
  val profile: SimplePgProfile.type = SimplePgProfile
  val db                            = Database.forConfig("database.main")
  //  }

}

trait DatabaseComponent {

  val profile: JdbcProfile

  val db: Database

}
