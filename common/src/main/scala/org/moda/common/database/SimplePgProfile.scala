package org.moda.common.database

import java.sql.{Date, Time, Timestamp}
import java.util.UUID
import com.github.tminglei.slickpg._
import slick.basic.Capability
import slick.jdbc.JdbcType

trait SimplePgProfile
  extends ExPostgresProfile
    with PgArraySupport
    with PgDate2Support
    with PgRangeSupport
    with PgHStoreSupport
    with PgSearchSupport
    with PgNetSupport
    with PgLTreeSupport {

  def pgjson = "jsonb"

  // Add back `capabilities.insertOrUpdate` to enable native `upsert` support; for postgres 9.5+
  override protected def computeCapabilities: Set[Capability] =
    super.computeCapabilities + slick.jdbc.JdbcCapabilities.insertOrUpdate

  override val api: API = MyAPI
  object MyAPI extends pgAPI
  trait pgAPI
    extends API
      with ArrayImplicits
      with DateTimeImplicits
      with NetImplicits
      with LTreeImplicits
      with RangeImplicits
      with HStoreImplicits
      with SearchImplicits
      with SimpleArrayPlainImplicits
      with SearchAssistants {

    implicit val simpleUUIDSeqTypeMapper: JdbcType[Seq[UUID]]     = new SimpleArrayJdbcType[UUID]("uuid").to(_.toSeq)
    implicit val simpleStrSeqTypeMapper: JdbcType[Seq[String]]    = new SimpleArrayJdbcType[String]("text").to(_.toSeq)
    implicit val simpleLongSeqTypeMapper: JdbcType[Seq[Long]]     = new SimpleArrayJdbcType[Long]("int8").to(_.toSeq)
    implicit val simpleIntSeqTypeMapper: JdbcType[Seq[Int]]       = new SimpleArrayJdbcType[Int]("int4").to(_.toSeq)
    implicit val simpleShortSeqTypeMapper: JdbcType[Seq[Short]]   = new SimpleArrayJdbcType[Short]("int2").to(_.toSeq)
    implicit val simpleFloatSeqTypeMapper: JdbcType[Seq[Float]]   = new SimpleArrayJdbcType[Float]("float4").to(_.toSeq)
    implicit val simpleDoubleSeqTypeMapper: JdbcType[Seq[Double]] = new SimpleArrayJdbcType[Double]("float8").to(_.toSeq)
    implicit val simpleBoolSeqTypeMapper: JdbcType[Seq[Boolean]]  = new SimpleArrayJdbcType[Boolean]("bool").to(_.toSeq)
    implicit val simpleDateSeqTypeMapper: JdbcType[Seq[Date]]     = new SimpleArrayJdbcType[Date]("date").to(_.toSeq)
    implicit val simpleTimeSeqTypeMapper: JdbcType[Seq[Time]]     = new SimpleArrayJdbcType[Time]("time").to(_.toSeq)
    implicit val simpleTsSeqTypeMapper: JdbcType[Seq[Timestamp]]  = new SimpleArrayJdbcType[Timestamp]("timestamp").to(_.toSeq)

  }

}

object SimplePgProfile extends SimplePgProfile
