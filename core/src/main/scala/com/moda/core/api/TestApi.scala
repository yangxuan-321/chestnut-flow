package com.moda.core.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.zz.cdp.monitor.database.DatabaseComponent

import scala.util._


object MainApi {

  def apply()(implicit dc: DatabaseComponent): MainApi = new MainApi()

}

/**
 * @author huanggh
 * 2020/9/4 下午4:33
 */
class TestApi(implicit dc: DatabaseComponent) extends Api {

  val dao: FooDAO = FooDAO()

  val oceanDAO: OceanengineSyncTaskDAO = OceanengineSyncTaskDAO()

  val mainR: Route = path("main") {
    complete("Ok")
  }

  val query: Route = path("slick" / "query") {
    val q = dao.query()
    onComplete(q) {
      case Success(value)  => complete(value.map(_.name).mkString(","))
      case Failure(exception)      => exception.printStackTrace()
        complete("failure")
    }
  }

  val queryBasic: Route = path("slick" / "basic") {
    val q = oceanDAO.query()
    onComplete(q) {
      case Success(value)  => complete(value.map(_.siteSyncTs).mkString(","))
      case Failure(exception)      => exception.printStackTrace()
        complete("failure")
    }
  }

  override val routes: Route = mainR ~ query ~ queryBasic

}
