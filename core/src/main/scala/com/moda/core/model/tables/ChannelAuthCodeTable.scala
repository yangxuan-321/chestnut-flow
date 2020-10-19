package com.moda.core.model.tables

import java.sql.Timestamp

import com.zz.cdp.monitor.database.{DatabaseComponent, HasDatabaseComponent, PgColumnMapping}
import com.zz.cdp.monitor.model.ColumnTypesMapper
import com.zz.idl.monitor.{AuthCodeStatus, ChannelAuthCode, ChannelType}
import slick.collection.heterogeneous.HNil

@SuppressWarnings(Array("org.wartremover.warts.Nothing"))
trait ChannelAuthCodeTable {

  this: ColumnTypesMapper with PgColumnMapping =>
  import DatabaseComponent.profile.api._

  val channelAuthCodes: TableQuery[ChannelAuthCodes] =
    TableQuery[ChannelAuthCodes]((tag: Tag) => new ChannelAuthCodes(tag, "channel_auth_code"))

  class ChannelAuthCodes(tag: Tag, tableName: String) extends Table[ChannelAuthCode](tag, tableName) {

    def id                  = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def userId: Rep[Long]              = column[Long]("user_id", O.SqlType("BIGINT"), O.Default(0L))
    def authCode: Rep[String]          = column[String]("auth_code", O.SqlType("TEXT"), O.Default(""))
    def channelType: Rep[ChannelType]  = column[ChannelType]("channel_type", O.SqlType("SMALLINT"), O.Default(ChannelType.UNKNOWN_CHANNEL))
    def status: Rep[AuthCodeStatus]    = column[AuthCodeStatus]("status", O.SqlType("SMALLINT"), O.Default(AuthCodeStatus.READY_TOKEN))
    def createdAt: Rep[Timestamp]      = column[Timestamp]("created_at", O.SqlType("timestamptz default now()"))
    def updatedAt: Rep[Timestamp]      = column[Timestamp]("updated_at", O.SqlType("timestamptz default now()"))

    def * =
      (
        id ::
          userId ::
          authCode ::
          channelType ::
          status ::
          createdAt.mapToInstant ::
          updatedAt.mapToInstant ::
          HNil
      ).mapTo[ChannelAuthCode]
  }

}
