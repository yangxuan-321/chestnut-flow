package com.moda.core.model.tables

import java.sql.Timestamp

import com.zz.cdp.monitor.database.{DatabaseComponent, HasDatabaseComponent, PgColumnMapping}
import com.zz.cdp.monitor.model.ColumnTypesMapper
import com.zz.idl.monitor.{AccountType, ChannelType, PlatformAdvertiserInfo, Status}
import slick.collection.heterogeneous.HNil


@SuppressWarnings(Array("org.wartremover.warts.Nothing"))
trait PlatformAdvertiserInfoTable {

//  this: HasDatabaseComponent with ColumnTypesMapper with PgColumnMapping =>
  this: ColumnTypesMapper with PgColumnMapping =>

//  import dc.profile.api._
  import DatabaseComponent.profile.api._

  val platformAdvertisers: TableQuery[PlatformAdvertisers] =
    TableQuery[PlatformAdvertisers]((tag: Tag) => new PlatformAdvertisers(tag, "platform_advertiser_info"))

  class PlatformAdvertisers(tag: Tag, tableName: String) extends Table[PlatformAdvertiserInfo](tag, tableName) {

    def userId: Rep[Long]                = column[Long]("user_id", O.SqlType("BIGINT"), O.Default(0L))
    def channelType: Rep[ChannelType]    = column[ChannelType]("channel_type", O.SqlType("SMALLINT"), O.Default(ChannelType.UNKNOWN_CHANNEL))
    def advertiserId: Rep[Long]          = column[Long]("advertiser_id", O.SqlType("BIGINT"), O.Default(0L))
    def name: Rep[String]                = column[String]("name", O.SqlType("TEXT"), O.Default(""))
    def accountType: Rep[AccountType]    = column[AccountType]("account_type", O.Default(AccountType.ADVERTISER_ACCOUNT))
    def company: Rep[String]             = column[String]("company", O.SqlType("TEXT"), O.Default(""))
    def authCode: Rep[String]            = column[String]("auth_code", O.SqlType("TEXT"), O.Default(""))
    def accessToken: Rep[String]         = column[String]("access_token", O.SqlType("TEXT"), O.Default(""))
    def expiresIn: Rep[Long]             = column[Long]("expires_in", O.SqlType("BIGINT"), O.Default(0L))
    def refreshToken: Rep[String]        = column[String]("refresh_token", O.SqlType("TEXT"), O.Default(""))
    def refreshTokenExpiresIn: Rep[Long] = column[Long]("refresh_token_expires_in", O.SqlType("BIGINT"), O.Default(0L))
    def status: Rep[Status]              = column[Status]("status", O.SqlType("SMALLINT"), O.Default(Status.Inactive))
    def balance: Rep[Double]             = column[Double]("balance", O.Default(0.0))
    def isDeleted: Rep[Boolean]          = column[Boolean]("is_deleted", O.Default(false))
    def tokenUpdatedAt: Rep[Timestamp]   = column[Timestamp]("token_updated_at", O.SqlType("timestamptz default now()"))
    def createdAt: Rep[Timestamp]        = column[Timestamp]("created_at", O.SqlType("timestamptz default now()"))
    def updatedAt: Rep[Timestamp]        = column[Timestamp]("updated_at", O.SqlType("timestamptz default now()"))

    def * =
      (
        userId ::
          channelType ::
          advertiserId ::
          name ::
          accountType ::
          company ::
          authCode ::
          accessToken ::
          expiresIn ::
          refreshToken ::
          refreshTokenExpiresIn ::
          status ::
          balance ::
          isDeleted ::
          tokenUpdatedAt.mapToInstant ::
          createdAt.mapToInstant ::
          updatedAt.mapToInstant ::
          HNil
      ).mapTo[PlatformAdvertiserInfo]
  }

}
