package org.moda.core

import java.util.UUID

import akka.actor.typed.SpawnProtocol.Command
import akka.actor.typed._
import akka.http.scaladsl.Http
import com.typesafe.scalalogging.Logger
import org.moda.core.database.{DatabaseComponent, DatabaseExtension}
import org.moda.core.http.AkkaHttpServer
//import com.zz.cdp.monitor.grpc.ThrottleGrpcClient
//import com.zz.cdp.monitor.streams.{ApiThrottleMonitor, BackstageMonitor, OAuthMonitor, RefreshTokenMonitor}
import pureconfig.ConfigSource

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object Main extends App {

  import scala.concurrent.ExecutionContext.Implicits.global

  val logger: Logger = Logger(getClass)

  val systemId: String                      = ConfigSource.default.at("akka.system.name").loadOrThrow[String]
  val selfUuid: String                      = UUID.randomUUID().toString
  implicit val system: ActorSystem[Command] = ActorSystem(SpawnProtocol(), systemId)
  implicit val dc: DatabaseComponent        = DatabaseExtension(system).databaseComponent

  val bindingFuture: Future[Http.ServerBinding] = AkkaHttpServer().server()

  sys.addShutdownHook {
    bindingFuture.flatMap(_.unbind()).onComplete { _ =>
      logger.info("System Terminated. Goodbye.")
    }
  }

//  ThrottleGrpcClient().run()

//  import io.circe.syntax._
//  import io.circe.generic.auto._
//  val dao = OceanengineSyncTaskDAO()
//  dao.query().foreach { x =>
//    logger.info("task: {}", x.asJson.spaces2)
//  }

//  StreamsManager.start()

//  BasicDataSyncMonitor().flow().run()

  Await.result(system.whenTerminated, Duration.Inf)
}
