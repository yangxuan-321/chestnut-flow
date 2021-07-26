package org.moda.core.stream

import akka.actor.typed.ActorSystem
import akka.stream.scaladsl.{Keep, RestartSource, RunnableGraph, Sink, Source}
import akka.stream.{ActorAttributes, KillSwitches, Supervision, UniqueKillSwitch}
import com.typesafe.scalalogging.Logger
import org.moda.common.database.DatabaseComponent
import org.moda.core.bo.MacroFlowBO
import org.moda.idl.ChestnutNodeInstance
import org.moda.idl.FlowNodeType._

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.control.NonFatal

/**
 * 将自动节点自动执行流
 * TODO 没做完，有空做
 */
object FlowAutoNodeExecuteStream {
  def apply()(implicit dc: DatabaseComponent, system: ActorSystem[_]): FlowAutoNodeExecuteStream
    = new FlowAutoNodeExecuteStream()
}

class FlowAutoNodeExecuteStream(implicit dc: DatabaseComponent, implicit val system: ActorSystem[_]) {

  import scala.concurrent.ExecutionContext.Implicits.global

  private[this] val logger = Logger(getClass)

  private[this] val macroFlowBO: MacroFlowBO = MacroFlowBO()

  def run(): UniqueKillSwitch = runGraph().run()

  private[this] def runGraph(): RunnableGraph[UniqueKillSwitch] = {
    RestartSource.withBackoff(minBackoff = 1.seconds, maxBackoff = 60.seconds, randomFactor = 0.2) { () =>
      Source.future {
        Source.tick(1.second, 100.millis, ())
          .mapAsync(1) {
            _ => queryAutoTaskNodeInstanceAllInfo()
          }
          .map {x =>
            logger.info("already => {}", x)
            x
          }
          .mapConcat(x1 => x1)
          .mapAsync(1) {x2 =>
            // 将事件发到具体的actor
            logger.info("将事件发送至actor啦!...")
            Future { x2 }
          }
          .withAttributes(ActorAttributes.supervisionStrategy({
            case NonFatal(e) =>
              logger.error("run graph error: {}", e.getMessage)
              e.printStackTrace()
              Supervision.Resume
          }))
          .runWith(Sink.ignore)
      }
    }
    .viaMat(KillSwitches.single)(Keep.right)
    .toMat(Sink.ignore)(Keep.left)
  }

  // 查询自动节点要做的事情
  def queryAutoTaskNodeInstanceAllInfo(): Future[Vector[ChestnutNodeInstance]] = {
    macroFlowBO.queryTaskInstanceInfo(
      // 人工节点 判断节点 结束节点 自动节点 合并节点
      Vector(FLOW_NODE_TYPE_START, FLOW_NODE_TYPE_SWITCH, FLOW_NODE_TYPE_END, FLOW_NODE_TYPE_AUTO, FLOW_NODE_TYPE_MERGE)
    )
  }
}
