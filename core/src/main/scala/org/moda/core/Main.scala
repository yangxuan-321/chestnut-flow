package org.moda.core

import java.util.UUID

import akka.actor.typed.SpawnProtocol.Command
import akka.actor.typed._
import akka.http.scaladsl.Http
import com.typesafe.scalalogging.Logger
import org.moda.common.database.{DatabaseComponent, DatabaseExtension}
import org.moda.core.http.AkkaHttpServer
import org.moda.mongo.config.MongoConn
import pureconfig.ConfigSource
import reactivemongo.api.DB

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}


object Main extends App {

  import scala.concurrent.ExecutionContext.Implicits.global

  val logger: Logger = Logger(getClass)

  val systemId: String                      = ConfigSource.default.at("akka.system.name").loadOrThrow[String]
  val selfUuid: String                      = UUID.randomUUID().toString
  implicit val system: ActorSystem[Command] = ActorSystem(SpawnProtocol(), systemId)
  implicit val dc: DatabaseComponent        = DatabaseExtension(system).databaseComponent
  implicit val mongo: DB = Await.result(MongoConn.chestnutMongo, 5.minutes)

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
