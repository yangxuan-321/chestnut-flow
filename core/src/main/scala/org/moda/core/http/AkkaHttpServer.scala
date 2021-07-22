package org.moda.core.http

import akka.actor
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.adapter._
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.Logger
import org.moda.auth.api.Api
import org.moda.auth.middleware.TokenAuthenticate
import org.moda.common.database.DatabaseComponent
import org.moda.core.api.{AuthUserApi, BackFlowApi, MacroFlowApi, SimpleMongoApi}
import reactivemongo.api.DB

import scala.concurrent.Future

object AkkaHttpServer {

  def apply()(implicit system: ActorSystem[_], mat: Materializer, dc: DatabaseComponent, mongo: DB) = new AkkaHttpServer()

}

class AkkaHttpServer(implicit system: ActorSystem[_], mat: Materializer, dc: DatabaseComponent, mongo: DB) {

  //  implicit val ec: ExecutionContextExecutor = system.toClassic.dispatcher

  private[this] val config: Config = ConfigFactory.load()

  private[this] val logger = Logger(getClass)

  private[this] val auth: TokenAuthenticate = TokenAuthenticate(dc)

  def server(): Future[Http.ServerBinding] = {
    val address         = config.getString("http.akka.host")
    val port            = config.getInt("http.akka.port")
    val apis: List[Api] = List(
      AuthUserApi(),
      SimpleMongoApi(),
      BackFlowApi(),
      MacroFlowApi()
    )
//    val routes = apis.map { ax =>
//      ax.publicR ~ auth.authenticate {x => ax.authedR(x)}
//    }.reduceLeft(_ ~ _)
    val publicRoutes = apis.filter(_.publicR.nonEmpty).map(_.publicR.get).reduceLeft(_ ~ _)
    val authRoutes = apis.filter(_.authedR.nonEmpty).map {ax =>
      auth.authenticate {x =>
        val f = ax.authedR.get
        f(x)
      }
    }.reduceLeft(_ ~ _)
    val routes = publicRoutes ~ authRoutes

    implicit val _system: actor.ActorSystem = system.toClassic
    val i = Http().bindAndHandle(routes, address, port)
    val stream = getClass.getResourceAsStream("/issue.txt")
    val text = scala.io.Source.fromInputStream(stream).mkString.replace("chestnut!", s"""Server online at http://$address:$port/ ...""")
    logger.info(text)
    i
  }

}
