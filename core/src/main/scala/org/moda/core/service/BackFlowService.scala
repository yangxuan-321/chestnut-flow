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

}