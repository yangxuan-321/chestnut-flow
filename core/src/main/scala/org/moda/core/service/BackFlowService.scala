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

