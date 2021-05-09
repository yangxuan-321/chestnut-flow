package org.moda.core.service

import com.typesafe.scalalogging.Logger
import org.moda.common.database.DatabaseComponent
import reactivemongo.api.DB
import reactivemongo.api.bson.BSONDocument
import reactivemongo.api.bson.collection.BSONCollection
import reactivemongo.api.commands.WriteResult

import scala.concurrent.Future

object MongoService {
  def apply()(implicit dc: DatabaseComponent, mongo: DB): MongoService = new MongoService()
}

class MongoService(implicit dc: DatabaseComponent, mongo: DB) {
  import scala.concurrent.ExecutionContext.Implicits.global
  val logger: Logger = Logger(getClass)
  val nodeCollection: BSONCollection = mongo[BSONCollection]("node")

  /** 根据用户id获取用户并且更新最新访问时间 **/
  def createNode(): Future[Int] = {
//    val query = BSONDocument()
//    val cursor = nodeCollection.find(query).cursor[BSONDocument]
//    val futureList = cursor.toList
    val r = nodeCollection.insert(ordered = false).one(BSONDocument(
      "id"      ->  1,
      "name"    ->  "录入",
      "next"    ->  "审核"
    ))

    r.map(_.code.getOrElse(-1))
  }
}

