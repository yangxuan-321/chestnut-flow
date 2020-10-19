package com.moda.core.dao

import akka.actor.typed.ActorSystem
import com.typesafe.scalalogging.Logger
import com.zz.cdp.monitor.database.DatabaseComponent
import com.zz.cdp.monitor.model.{Foo, Tables}
import com.zz.cdp.monitor.model.tables.FooTable
import com.zz.idl.monitor.ChannelType

import scala.concurrent.{ExecutionContext, Future}

/**
 * @author huanggh
 * 2020/9/11 下午2:52
 */
object TestDAO {
  def apply()(implicit dcc: DatabaseComponent): TestDAO = new TestDAO {
    override val dc: DatabaseComponent = dcc
  }
}

trait TestDAO extends DAO {

//  override val dc: DatabaseComponent = dcc

  import dc.profile.api._

  val logger: Logger = Logger(getClass)

  val fooTable: FooTable = new Tables(dc)

  def query(): Future[Seq[Foo]] = {
    val q = fooTable.foos.result
//    logger.info("sql: {}", q.statements.mkString(","))
    dc.db.run(q)
  }


}
