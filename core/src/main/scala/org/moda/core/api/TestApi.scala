package org.moda.core.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.circe.generic.auto._
import io.circe.syntax._
import org.moda.core.dao.TestUserDAO
import org.moda.core.database.DatabaseComponent

import scala.util._


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
        case Success(value)  => complete(value.asJson.toString)
        case Failure(exception)      => exception.printStackTrace()
          complete("failure")
      }
    }
  }

  override val routes: Route = mainR ~ query

}
