package com.moda.core.model.tables

import java.sql.Timestamp

import com.zz.cdp.monitor.database.{DatabaseComponent, PgColumnMapping}
import com.zz.cdp.monitor.model.ColumnTypesMapper
import com.zz.idl.monitor.{OceanengineSyncTask, Status}
import slick.collection.heterogeneous.HNil

@SuppressWarnings(Array("org.wartremover.warts.Nothing"))
trait OceanengineSyncTaskTable {


  this: ColumnTypesMapper with PgColumnMapping =>
  import DatabaseComponent.profile.api._

  val oceanengineSyncTasks: TableQuery[OceanengineSyncTasks] =
    TableQuery[OceanengineSyncTasks]((tag: Tag) => new OceanengineSyncTasks(tag, "oceanengine_sync_task"))

  class OceanengineSyncTasks(tag: Tag, tableName: String) extends Table[OceanengineSyncTask](tag, tableName) {

    def advertiserId: Rep[Long] =
      column[Long]("advertiser_id", O.SqlType("BIGINT"), O.Default(0L))
    def fundSyncTs: Rep[Timestamp] =
      column[Timestamp]("fund_sync_ts", O.SqlType("timestamptz default now()"))
    def campaignSyncTs: Rep[Timestamp] =
      column[Timestamp]("campaign_sync_ts", O.SqlType("timestamptz default now()"))
    def lineItemSyncTs: Rep[Timestamp] =
      column[Timestamp]("line_item_sync_ts", O.SqlType("timestamptz default now()"))
    def creativeSyncTs: Rep[Timestamp] =
      column[Timestamp]("creative_sync_ts", O.SqlType("timestamptz default now()"))
    def customAudienceSyncTs: Rep[Timestamp] =
      column[Timestamp]("custom_audience_sync_ts", O.SqlType("timestamptz default now()"))
    def imageSyncTs: Rep[Timestamp] =
      column[Timestamp]("image_sync_ts", O.SqlType("timestamptz default now()"))
    def videoSyncTs: Rep[Timestamp] =
      column[Timestamp]("video_sync_ts", O.SqlType("timestamptz default now()"))
    def siteSyncTs: Rep[Timestamp] =
      column[Timestamp]("site_sync_ts", O.SqlType("timestamptz default now()"))
    def landingurlSyncTs: Rep[Timestamp] =
      column[Timestamp]("landingurl_sync_ts", O.SqlType("timestamptz default now()"))
    def adverReportSyncTs: Rep[Timestamp] =
      column[Timestamp]("advertiser_report_sync_ts", O.SqlType("timestamptz default now()"))
    def adverHistoryReportSyncTs: Rep[Timestamp] =
      column[Timestamp]("advertiser_history_report_sync_ts", O.SqlType("timestamptz default now()"))
    def advertiserReportHourlySyncTs: Rep[Timestamp] =
      column[Timestamp]("advertiser_report_hourly_sync_ts", O.SqlType("timestamptz default now()"))
    def advertiserHistoryReportHourlySyncTs: Rep[Timestamp] =
      column[Timestamp]("advertiser_history_report_hourly_sync_ts", O.SqlType("timestamptz default now()"))
    def campaignReportSyncTs: Rep[Timestamp] =
      column[Timestamp]("campaign_report_sync_ts", O.SqlType("timestamptz default now()"))
    def campaignHistoryReportSyncTs: Rep[Timestamp] =
      column[Timestamp]("campaign_history_report_sync_ts", O.SqlType("timestamptz default now()"))
    def adReportSyncTs: Rep[Timestamp] =
      column[Timestamp]("ad_report_sync_ts", O.SqlType("timestamptz default now()"))
    def adHistoryReportSyncTs: Rep[Timestamp] =
      column[Timestamp]("ad_history_report_sync_ts", O.SqlType("timestamptz default now()"))
    def creativeReportSyncTs: Rep[Timestamp] =
      column[Timestamp]("creative_report_sync_ts", O.SqlType("timestamptz default now()"))
    def creativeHistoryReportSyncTs: Rep[Timestamp] =
      column[Timestamp]("creative_history_report_sync_ts", O.SqlType("timestamptz default now()"))
    def creativeReportHourlySyncTs: Rep[Timestamp] =
      column[Timestamp]("creative_report_hourly_sync_ts", O.SqlType("timestamptz default now()"))
    def creativeHistoryReportHourlySyncTs: Rep[Timestamp] =
      column[Timestamp]("creative_history_report_hourly_sync_ts", O.SqlType("timestamptz default now()"))
    def advertiserReportSyncEarliestTs: Rep[Timestamp] =
      column[Timestamp]("advertiser_report_sync_earliest_ts", O.SqlType("timestamptz default now()"))
    def advertiserReportHourlySyncEarliestTs: Rep[Timestamp] =
      column[Timestamp]("advertiser_report_hourly_sync_earliest_ts", O.SqlType("timestamptz default now()"))
    def campaignReportSyncEarliestTs: Rep[Timestamp] =
      column[Timestamp]("campaign_report_sync_earliest_ts", O.SqlType("timestamptz default now()"))
    def adReportSyncEarliestTs: Rep[Timestamp] =
      column[Timestamp]("ad_report_sync_earliest_ts", O.SqlType("timestamptz default now()"))
    def creativeReportSyncEarliestTs: Rep[Timestamp] =
      column[Timestamp]("creative_report_sync_earliest_ts", O.SqlType("timestamptz default now()"))
    def creativeReportHourlySyncEarliestTs: Rep[Timestamp] =
      column[Timestamp]("creative_report_hourly_sync_earliest_ts", O.SqlType("timestamptz default now()"))
    def status: Rep[Status] = column[Status]("status", O.SqlType("SMALLINT"), O.Default(Status.Inactive))

    def * =
      (
        advertiserId ::
          fundSyncTs.mapToInstant ::
          campaignSyncTs.mapToInstant ::
          lineItemSyncTs.mapToInstant ::
          creativeSyncTs.mapToInstant ::
          customAudienceSyncTs.mapToInstant ::
          imageSyncTs.mapToInstant ::
          videoSyncTs.mapToInstant ::
          siteSyncTs.mapToInstant ::
          landingurlSyncTs.mapToInstant ::
          adverReportSyncTs.mapToInstant ::
          adverHistoryReportSyncTs.mapToInstant ::
          advertiserReportHourlySyncTs.mapToInstant ::
          advertiserHistoryReportHourlySyncTs.mapToInstant ::
          campaignReportSyncTs.mapToInstant ::
          campaignHistoryReportSyncTs.mapToInstant ::
          adReportSyncTs.mapToInstant ::
          adHistoryReportSyncTs.mapToInstant ::
          creativeReportSyncTs.mapToInstant ::
          creativeHistoryReportSyncTs.mapToInstant ::
          creativeReportHourlySyncTs.mapToInstant ::
          creativeHistoryReportHourlySyncTs.mapToInstant ::
          advertiserReportSyncEarliestTs.mapToInstant ::
          advertiserReportHourlySyncEarliestTs.mapToInstant ::
          campaignReportSyncEarliestTs.mapToInstant ::
          adReportSyncEarliestTs.mapToInstant ::
          creativeReportSyncEarliestTs.mapToInstant ::
          creativeReportHourlySyncEarliestTs.mapToInstant ::
          status ::
          HNil
      ).mapTo[OceanengineSyncTask]
  }

}
