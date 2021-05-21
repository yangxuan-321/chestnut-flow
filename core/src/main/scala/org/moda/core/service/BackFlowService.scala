package org.moda.core.service

import java.io.File

import com.typesafe.scalalogging.Logger
import io.circe.generic.auto._
import io.circe.parser._
import org.moda.common.database.DatabaseComponent
import reactivemongo.api.DB

import scala.concurrent.Future
import scala.io.Source

object BackFlowService {
  def apply()(implicit dc: DatabaseComponent): BackFlowService = new BackFlowService()
}

class BackFlowService(implicit dc: DatabaseComponent) {
  val logger: Logger = Logger(getClass)
  import scala.concurrent.ExecutionContext.Implicits.global

  def typeGroups(): Future[Vector[TypeGroup]] = {
    val c = assembleTypeGroupData()
    val x = decode[Vector[TypeGroup]](c) match {
      case Left(l) => Vector.empty[TypeGroup]
      case Right(r) => r
    }

    Future {x}
  }

  private[this] def assembleTypeGroupData(): String = {
    val f = Source.fromInputStream(getClass.getResourceAsStream("/data/TypeGroupData.json"))
    val c: String = f.getLines().toList.foldLeft("")((r, e) => r + e)
    f.close()
    c
  }

  private[this] val processGroupMap: Map[String, ProcessGroup] = Map(
    "root"  ->  ProcessGroup(
      id = "b0ebc795-016a-1000-e402-2b387cc8d508",
      processors = Vector.empty[Processor],
      connections = Vector.empty[Connection],
      processGroups = Vector.empty[ProcessGroup],
      label = "",
      error = true,
      changed = true,
      status = Status(show = true, label = ""),
      rect = Rect(0, 0, 0, 0),
      icon = "",
      style = Style("red")
    )
  )

  def processGroups(id: String): Future[ProcessGroup] = {
    logger.info("processGroups => id => {}", id)
    val r: ProcessGroup = processGroupMap.find {
      case(k, v) => k == id && v.id == id
    }.get._2

    Future {r}
  }
}

case class TypeGroup(id: String, title: String, name: String, items: Vector[TypeItem])
case class TypeItem(
  id: String,
  name: String,
  icon: String,
  iconOnRight: Boolean,
  color: String,
  hasInput: Boolean,
  hasOutput: Boolean
)

case class ProcessGroup(
  id: String,
  processors: Vector[Processor],
  connections: Vector[Connection],
  processGroups: Vector[ProcessGroup],
  label: String,
  error: Boolean,
  changed: Boolean,
  status: Status,
  rect: Rect,
  icon: String,
  style: Style
)
case class Processor(
  id: String,
  typeId: String,
  oldID: String,
  label: String,
  hasInput: Boolean,
  hasOutput: Boolean,
  iconOnRight: Boolean,
  error: Boolean,
  changed: Boolean,
  status: Status,
  rect: Rect,
  icon: String,
  style: Style
)
case class Connection(id: String,  sourceId: String,  sourcePort: Int,  targetId: String)
case class Status(show: Boolean, label: String)
case class Rect(x: Int,  y: Int, h: Int, w: Int)
case class Style(color: String)