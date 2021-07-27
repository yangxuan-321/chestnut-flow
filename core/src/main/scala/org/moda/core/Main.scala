package org.moda.core

import java.util.UUID

import akka.actor.typed.SpawnProtocol.Command
import akka.actor.typed._
import akka.http.scaladsl.Http
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger
import org.moda.common.database.{DatabaseComponent, DatabaseExtension}
import org.moda.core.http.AkkaHttpServer
import org.moda.core.rpc.service.FlowRpcService
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

  private[this] val conf                    = ConfigFactory.load()
  private val akkaGrpcPort                  = conf.getInt("akka.grpc.server.port")

  implicit val system: ActorSystem[Command] = ActorSystem(SpawnProtocol(), systemId)
  implicit val dc: DatabaseComponent        = DatabaseExtension(system).databaseComponent
  implicit val mongo: DB = Await.result(MongoConn.chestnutMongo, 5.minutes)

  val bindingFuture: Future[Http.ServerBinding] = AkkaHttpServer().server()

  sys.addShutdownHook {
    bindingFuture.flatMap(_.unbind()).onComplete { _ =>
      logger.info("System Terminated. Goodbye.")
    }
  }

  // 集群
  startAkkaCluster()

  // 流
  StreamsManager.start()

  // rpc 服务
  for {
    _ <- new FlowRpcService().run("0.0.0.0", akkaGrpcPort)  // rpc服务
  } yield true

  Await.result(system.whenTerminated, Duration.Inf)

  def startAkkaCluster(): Option[ActorRef[_]] =
    ConfigSource.default
      .at("akka.cluster.seed-nodes")
      .load[Vector[String]] match {
      case Right(xs) if xs.nonEmpty => None
      case _                        => Option(ConsulAkkaNodeDiscoverer(selfUuid))
    }
}
