package org.moda.core.service

import com.typesafe.scalalogging.Logger
import org.moda.common.database.DatabaseComponent
import reactivemongo.api.DB
import reactivemongo.api.bson.BSONDocument
import reactivemongo.api.bson.collection.BSONCollection
import reactivemongo.api.commands.WriteResult

import scala.concurrent.Future

object BackFlowService {
  def apply()(implicit dc: DatabaseComponent, mongo: DB): MongoService = new MongoService()
}

class BackFlowService(implicit dc: DatabaseComponent, mongo: DB) {
  import scala.concurrent.ExecutionContext.Implicits.global
  val logger: Logger = Logger(getClass)

  def createNode(): Future[Int] = {

  }
}

