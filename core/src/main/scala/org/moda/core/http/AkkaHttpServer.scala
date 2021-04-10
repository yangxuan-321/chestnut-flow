package org.moda.core.http

import akka.actor
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.adapter._
import akka.http.scaladsl.Http
import akka.stream.Materializer
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.Logger
import org.moda.auth.api.Api
import org.moda.core.api.AuthUserApi

import scala.concurrent.Future
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.headers.RawHeader
import org.moda.common.database.DatabaseComponent

object AkkaHttpServer {

  def apply()(implicit system: ActorSystem[_], mat: Materializer, dc: DatabaseComponent) = new AkkaHttpServer()

}

class AkkaHttpServer(implicit system: ActorSystem[_], mat: Materializer, dc: DatabaseComponent) {

  //  implicit val ec: ExecutionContextExecutor = system.toClassic.dispatcher

  private[this] val config: Config = ConfigFactory.load()

  private[this] val logger = Logger(getClass)

  def server(): Future[Http.ServerBinding] = {
    val address         = config.getString("http.akka.host")
    val port            = config.getInt("http.akka.port")
    val apis: List[Api] = List(
      AuthUserApi()
    )
    val routes = apis.map(_.authedR).reduceLeft(_ ~ _)
    implicit val _system: actor.ActorSystem = system.toClassic
    val i = Http().bindAndHandle(routes, address, port)
    val stream = getClass.getResourceAsStream("/issue.txt")
    val text = scala.io.Source.fromInputStream(stream).mkString.replace("chestnut!", s"""Server online at http://$address:$port/ ...""")
    logger.info(text)
    i
  }

}
