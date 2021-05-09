package org.moda.mongo.config


import com.typesafe.config.{Config, ConfigFactory}
import org.moda.mongo.config.MongoConn.mongoUri
import reactivemongo.api.{DB, MongoConnection, MongoConnectionOptions}

import scala.concurrent.Future

object MongoConn extends MongoConn {
  import scala.concurrent.ExecutionContext.Implicits.global
  val driver = new reactivemongo.api.AsyncDriver

  // val connectOption: MongoConnectionOptions = MongoConnectionOptions(
  //   authenticationDatabase = Some(authDatabaseName),
  // )
  val chestnutMongo: Future[DB] = for {
    uri <- MongoConnection.fromString(mongoUri)
    con <- driver.connect(uri)
    db <- con.database(databaseName)
  } yield db

  chestnutMongo.onComplete {
    case resolution =>
      println(s"=============== resolution inited ===========: $resolution")
  }
}

trait MongoConn {
  private[this] val config: Config = ConfigFactory.load().getConfig("mongo.properties")
  val serverName: String = config.getString("serverName")
  val portNumber: Int = config.getInt("portNumber")
  val databaseName: String = config.getString("databaseName")
  val user: String = config.getString("user")
  val password: String = config.getString("password")
  val authDatabaseName: String = config.getString("authDatabaseName")

  val mongoUri = s"mongodb://$user:$password@$serverName:$portNumber/$databaseName?authSource=$authDatabaseName&readPreference=primary"
}
