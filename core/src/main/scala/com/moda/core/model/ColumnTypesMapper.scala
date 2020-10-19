package com.moda.core.model

import java.sql.Timestamp
import java.time.Instant

import com.zz.cdp.monitor.database.HasDatabaseComponent
import com.zz.idl.monitor.{AccountType, AuthCodeStatus, ChannelType, Status}
import slick.ast.BaseTypedType
import slick.lifted.MappedProjection

import scala.util.Try

import com.zz.cdp.monitor.database.DatabaseComponent

trait ColumnTypesMapper {

//  this: HasDatabaseComponent =>

//  import dc.profile.api._

  import DatabaseComponent.profile.api._

  private[this] val tF: (Timestamp => Long, Long => Option[Timestamp]) = (
    { t: Timestamp =>
      t.getTime
    }, { l: Long =>
      Option(new Timestamp(l))
    }
  )

  private[this] val tI: (Timestamp => Instant, Instant => Option[Timestamp]) = (
    { t: Timestamp =>
      t.toInstant
    }, { l: Instant =>
      Option(Timestamp.from(l))
    }
  )

  implicit class TimestampLongMapping(ts: Rep[Timestamp]) {

    def mapToLong: MappedProjection[Long, Timestamp] = ts <> (tF._1, tF._2)

  }

  implicit class TimestampInstantMapping(ts: Rep[Timestamp]) {

    def mapToInstant: MappedProjection[Instant, Timestamp] = ts <> (tI._1, tI._2)

  }

//  implicit val seqIntColumnType: BaseColumnType[Seq[Int]] =
//    MappedColumnType.base[Seq[Int], String](
//      _.map(_.toString).mkString(",")
//      , _.split(",").map(y => Try(y.trim.toInt).toOption).collect { case Some(i) => i}
//    )
//
//  implicit val seqLongColumnType: BaseColumnType[Seq[Long]] =
//    MappedColumnType.base[Seq[Long], String](
//      _.map(_.toString).mkString(",")
//      , _.split(",").map(y => Try(y.trim.toLong).toOption).collect { case Some(i) => i}
//    )
//
//  implicit val seqStringColumnType: BaseColumnType[Seq[String]] = MappedColumnType.base[Seq[String], String](
//    _.map(_.toString).mkString(","),
//    _.split(",").map(y => Try(y.trim).toOption).collect { case Some(i) => i }
//  )

  implicit val channelTypeColumnType: BaseTypedType[ChannelType]       = MappedColumnType.base(_.value, ChannelType.fromValue)
  implicit val authCodeStatusColumnType: BaseTypedType[AuthCodeStatus] = MappedColumnType.base(_.value, AuthCodeStatus.fromValue)
  implicit val accountTypeColumnType: BaseTypedType[AccountType]       = MappedColumnType.base(_.value, AccountType.fromValue)
  implicit val statusColumnType: BaseTypedType[Status]                 = MappedColumnType.base(_.value, Status.fromValue)

}
