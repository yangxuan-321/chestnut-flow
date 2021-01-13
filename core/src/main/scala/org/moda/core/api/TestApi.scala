package org.moda.core.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import org.moda.common.model.Pretty
import org.moda.core.dao.TestUserDAO
import org.moda.core.database.DatabaseComponent
import org.moda.idl.TestUserPO
import io.circe
import io.circe.{Decoder, Encoder}
import io.circe.syntax._
import io.circe.generic.auto._

import scala.util._
import org.moda.common.json.Json2String._



object TestApi {

  def apply()(implicit dc: DatabaseComponent): TestApi = new TestApi()

}

/**
 * @author moda-matser
 * 2020/9/4 下午4:33
 */
class TestApi(implicit dc: DatabaseComponent) extends Api {

  val dao: TestUserDAO = TestUserDAO()

  val mainR: Route = path("main") {
    complete("Ok")
  }

  val query: Route = path("user" / "query") {
    get {
      val q = dao.query()
      onComplete(q) {
        case Success(value)  =>
          val res: Pretty[Seq[TestUserPO]] = new Pretty[Seq[TestUserPO]](200, "success", value)
          complete(res.toJsonString)
        case Failure(exception)      =>
          // exception.printStackTrace()
          
          complete("failure")
      }
    }
  }

  override val routes: Route = mainR ~ query

}
