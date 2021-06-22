package com.zz.cdp.common.util

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.{Instant, ZoneId, ZoneOffset, ZonedDateTime}
import java.time.format.DateTimeFormatter
import java.util.{Calendar, Date, GregorianCalendar}

import scala.util.Try

object TimeTransUtil {

  val zoneId: ZoneId = ZoneId.of("Asia/Shanghai")

  def instant2Calendar(t: Instant): Calendar = {
    GregorianCalendar.from(ZonedDateTime.ofInstant(t, zoneId))
  }

  /**
   * 时间戳转时间字符串
   * @param timestamp 时间戳
   * @return
   */
  def timestamp2String(timestamp: Long): String = {
    val simpleFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val date: Date = new Date(timestamp)
    simpleFormat.format(date)
  }

  /**
   * 时间戳转时间字符串
   * @param timestamp 时间戳
   * @return
   */
  def timestamp2String3(timestamp: Long): String = {
    val simpleFormat: SimpleDateFormat = new SimpleDateFormat("yyMMdd")
    val date: Date = new Date(timestamp)
    simpleFormat.format(date)
  }

  def timestamp2String2(timestamp: Long): String = {
    val simpleFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date: Date = new Date(timestamp)
    simpleFormat.format(date)
  }

  def timestamp2String5(timestamp: Long): String = {
    val simpleFormat: SimpleDateFormat = new SimpleDateFormat("HH:mm")
    val date: Date = new Date(timestamp)
    simpleFormat.format(date)
  }

  /**
   * 获取今天的时间
   * @param date date
   * @return
   */
  def getToday(date: Date): Long = {
    val format: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val str: String = format.format(date)
    val dt = format.parse(str)
    dt.getTime
  }

  /**
   * 时间字符串转时间戳
   * @param s ss
   * @return
   */
  def stringToTimestamp(s: String): Long = {
    val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date = simpleDateFormat.parse(s)
    date.getTime
  }

  /**
   * 时间字符串转时间戳
   * @param s ss
   * @return
   */
  def stringToInstant(s: String): Instant = {
    val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date: Date = simpleDateFormat.parse(s)
    Instant.ofEpochMilli(date.getTime)
  }

  def stringToInstant2(s: String): Instant = {
    Try {
      val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm")
      val date: Date = simpleDateFormat.parse(s)
      Instant.ofEpochMilli(date.getTime)
    } getOrElse stringToInstant(s)
  }

  def stringDateToInstant(s: String): Instant = {
    val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val date: Date = simpleDateFormat.parse(s)
    Instant.ofEpochMilli(date.getTime).atZone(zoneId).toInstant
  }

  def stringDateToLong(s: String): Long = {
    val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val date: Date = simpleDateFormat.parse(s)
    date.getTime
  }

  def sharpByDay(ts: Long): Instant =
    Instant
      .ofEpochMilli(ts)
      .atZone(zoneId)
      .withHour(0)
      .withMinute(0)
      .withSecond(0)
      .withNano(0)
      .toInstant

  /**
   * 时间转字符串
   * @param date 时间
   * @return
   */
  def dateToString(date: Date): String = {
    val format: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val str: String = format.format(date)
    str
  }

  /**
   * 获取当天凌晨时间戳
   * @return
   */
  def getZeroTime: Long = {
    getTime()
  }

  def getTime(hour: Int = 0, minute: Int = 0, second: Int = 0, nano: Int = 0): Long = {
    val t2: ZonedDateTime = ZonedDateTime.now(zoneId)
    val t1: ZonedDateTime = t2
      .withHour(hour)
      .withMinute(minute)
      .withSecond(second)
      .withNano(nano)
    t1.toInstant.toEpochMilli
  }

  def getTimeSecond: Int = {
    val t: ZonedDateTime = ZonedDateTime.now(zoneId)
    t.getSecond
  }

  def nowDateToString: String = {
    val format: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val str: String = format.format(new Date())
    str
  }

  def getZeroInstant: Instant = {
    val t2: ZonedDateTime = ZonedDateTime.now(zoneId)
    val t1: ZonedDateTime = t2
      .withHour(0)
      .withMinute(0)
      .withSecond(0)
      .withNano(0)
    t1.toInstant
  }

  def getTimePlusDay(day: Int): Long = {
    val t2: ZonedDateTime = ZonedDateTime.now(zoneId)
    val t1: ZonedDateTime = t2
      .withHour(0)
      .withMinute(0)
      .withSecond(0)
      .withNano(0)
      .plusDays(day)
    t1.toInstant.toEpochMilli
  }

  def getInstantPlusDay(day: Int, t: Instant): Instant = {
    t
      .atZone(zoneId)
      .withHour(0)
      .withMinute(0)
      .withSecond(0)
      .withNano(0)
      .plusDays(day)
      .toInstant
  }

  def getInstantPlusMonth(month: Int, t: Instant): Instant = {
    t
      .atZone(zoneId)
      .withHour(0)
      .withMinute(0)
      .withSecond(0)
      .withNano(0)
      .plusMonths(month)
      .toInstant
  }

  def getInstantPlusYear(year: Int, t: Instant): Instant = {
    t
      .atZone(zoneId)
      .withHour(0)
      .withMinute(0)
      .withSecond(0)
      .withNano(0)
      .plusYears(year)
      .toInstant
  }

  def getInstantPlusDayToString(day: Int, t: Instant): String = {
    val simpleFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val date: Date = new Date(getInstantPlusDay(day, t).toEpochMilli)
    simpleFormat.format(date)
  }

  def instantToString(t: Instant): String = {
    val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm")
    val date: Date = new Date(t.toEpochMilli)
    simpleDateFormat.format(date)
  }

  def instantToString2(t: Instant): String = {
    val simpleDateFormat = new SimpleDateFormat("yyyyMMdd HHmmss")
    val date: Date = new Date(t.toEpochMilli)
    simpleDateFormat.format(date)
  }

  def instantToString3(t: Instant): String = {
    val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val date: Date = new Date(t.toEpochMilli)
    simpleDateFormat.format(date)
  }

  def instantToTimeString(t: Instant): String = {
    val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date: Date = new Date(t.toEpochMilli)
    simpleDateFormat.format(date)
  }

  def getTimeMinusHour(hour: Int): Instant = {
    val t2: ZonedDateTime = ZonedDateTime.now(zoneId)
    val t1: ZonedDateTime = t2
      .minusHours(hour)
    t1.toInstant
  }

  def getTimeMinusMinute(minute: Int): Instant = {
    val t2: ZonedDateTime = ZonedDateTime.now(zoneId)
    val t1: ZonedDateTime = t2
      .minusMinutes(minute)
    t1.toInstant
  }

  /**
   * 获取当天晚上 23:59:59 时间戳
   * @return
   */
  def getLatestTime: Long = {
    val t2: ZonedDateTime = ZonedDateTime.now(zoneId)
    val t1: ZonedDateTime = t2
      .withHour(23)
      .withMinute(59)
      .withSecond(59)
    t1.toInstant.toEpochMilli
  }

  def getFiveMinuteFirstByMinute: (Long, Long) = {
    val t3: ZonedDateTime = ZonedDateTime.now(zoneId)
    val t1: ZonedDateTime = t3
      .plusMinutes(-5)
      .withSecond(0)
      .withNano(0)
    val t2: ZonedDateTime = t3
      .withSecond(0)
      .withNano(0)
    (t1.toInstant.toEpochMilli, t2.toInstant.toEpochMilli)
  }

  def getCurrentHour: Int = {
    val t = ZonedDateTime.now(zoneId)
    t.getHour
  }

  def getCurrentInstant: Instant = {
    val t = ZonedDateTime.now(zoneId)
    t.toInstant
  }

  def getTimestampHour(ts: Timestamp): Int = {
    val t = ZonedDateTime.ofInstant(ts.toInstant, zoneId)
    t.getHour
  }

  def long2Timestamp(ts: Long): Timestamp = {
    val t = ZonedDateTime.ofInstant(Instant.ofEpochMilli(ts), zoneId)
    Timestamp.from(t.toInstant)
  }

  def long2Instant(ts: Long): Instant = {
    val t = ZonedDateTime.ofInstant(Instant.ofEpochMilli(ts), zoneId)
    t.toInstant
  }

  def timestampToString(ts: Timestamp): String = {
    Instant.ofEpochMilli(ts.getTime)
      .atOffset(ZoneOffset.ofHours(8))
      .format(DateTimeFormatter.ISO_DATE_TIME)
  }

}
