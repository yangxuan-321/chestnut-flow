package org.moda.core.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.circe.generic.auto._
import org.moda.auth.api.{Api, ApiError, Pretty}
import org.moda.common.database.DatabaseComponent
import org.moda.common.json.FailFastCirceSupport._
import org.moda.core.service.MongoService
import org.moda.idl._
import reactivemongo.api.DB

import scala.util.{Failure, Success}


/**
 * @author moda-matser
 * 2020/9/4 下午4:33
 */
object SimpleMongoApi {
  def apply()(implicit dc: DatabaseComponent, mongo: DB): SimpleMongoApi = new SimpleMongoApi()
}
class SimpleMongoApi(implicit dc: DatabaseComponent, mongo: DB) extends Api {
  val mongoService: MongoService = MongoService()

  val mongoNodeR0: Route = path("v0" / "mongo" / "node") {
      get {
        val q = mongoService.createNode()
        onComplete(q) {
          case Success(value)  =>
            val res = Pretty(value)
            complete(res)
          case Failure(exception)      =>
            // exception.printStackTrace()
            complete(ApiError.internalServerError)
        }
      }
    }

  override def publicR: Route = mongoNodeR0

  override def authedR: SimpleAuthUser => Route = ???
}
