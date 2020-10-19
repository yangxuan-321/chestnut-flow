package org.moda.common.database

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
