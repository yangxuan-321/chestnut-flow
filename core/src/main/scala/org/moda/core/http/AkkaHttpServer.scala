package org.moda.core.http

import akka.actor
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.adapter._
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import org.moda.core.api.TestApi
import com.typesafe.config.{Config, ConfigFactory}
import org.moda.core.api._
import com.typesafe.scalalogging.Logger
import org.moda.core.api.{Api, TestApi}
import org.moda.core.database.DatabaseComponent

import scala.concurrent.{ExecutionContextExecutor, Future}

object AkkaHttpServer {

  def apply()(implicit system: ActorSystem[_], mat: Materializer, dc: DatabaseComponent) = new AkkaHttpServer()

}

class AkkaHttpServer(implicit system: ActorSystem[_], mat: Materializer, dc: DatabaseComponent) {

  import scala.concurrent.ExecutionContext.Implicits.global

  //  implicit val ec: ExecutionContextExecutor = system.toClassic.dispatcher

  private[this] val config: Config = ConfigFactory.load()

  private[this] val logger = Logger(getClass)

  def server(): Future[Http.ServerBinding] = {
    val address         = config.getString("http.akka.host")
    val port            = config.getInt("http.akka.port")
    val apis: List[Api] = List(
      TestApi()
    )
    val routes = apis.map(_.routes).reduceLeft(_ ~ _)
    implicit val _system: actor.ActorSystem = system.toClassic
    val i = Http().bindAndHandle(routes, address, port)
    val stream = getClass.getResourceAsStream("/issue.txt")
    val text = scala.io.Source.fromInputStream(stream).mkString.replace("chestnut!", s"""Server online at http://$address:$port/ ...""")
    logger.info(text)
    i
  }

}