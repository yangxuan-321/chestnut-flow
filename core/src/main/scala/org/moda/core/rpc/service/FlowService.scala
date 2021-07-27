package org.moda.core.rpc.service

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.adapter._
import akka.http.scaladsl.Http
import com.typesafe.scalalogging.Logger
import org.moda.auth.service.UserService
import org.moda.common.database.DatabaseComponent
import org.moda.core.service.MacroFlowService
import org.moda.idl.WorkFlowServiceStartReq
import org.moda.idl.rpc.{RpcFlowService, RpcFlowServiceHandler, RpcWorkFlowServiceStartRes}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


private[core] class FlowService(implicit dc: DatabaseComponent) extends RpcFlowService {

  import scala.concurrent.ExecutionContext.Implicits.global

  val logger: Logger = Logger(getClass)

  private[this] val macroFlowService: MacroFlowService = MacroFlowService()
  private[this] val userService: UserService = UserService()

  override def startFlow(in: WorkFlowServiceStartReq): Future[RpcWorkFlowServiceStartRes] = {
    for {
      a <- userService.queryAdminUserInfo()
      b <- macroFlowService.startWorkFlow(in, a)
    } yield b match {
      case Right(r) if r > 0 => RpcWorkFlowServiceStartRes(status = 0, flowInstanceId = Some(r), message = Some("成功"))
      case Left(l) => RpcWorkFlowServiceStartRes(status = 500, message = Some(l))
    }
  }
}


// core项目才能使用
private[core] class FlowRpcService {
  import scala.concurrent.ExecutionContext.Implicits.global

  def run(host: String, port: Int)(implicit system: ActorSystem[_], dc: DatabaseComponent):
      Future[Http.ServerBinding] = {
    val service = RpcFlowServiceHandler(new FlowService())(system.toClassic)
    val binding = Http()(system.toClassic).bindAndHandleAsync(service, host, port)
    binding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        println("gRPC server bound to {}:{}", address.getHostString, address.getPort)
      case Failure(ex) =>
        println("Failed to bind gRPC endpoint, terminating system", ex)
        system.terminate()
    }

    binding
  }
}
