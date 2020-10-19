package com.zz.cdp.monitor.database

import java.time.format.DateTimeFormatter
import java.time.{Instant, ZoneOffset}
import java.sql.Timestamp

object PgColumnMapping extends PgColumnMapping

trait PgColumnMapping {

  implicit class T2L(t: Timestamp) {
    def  asLong:Long = t.getTime
  }

  implicit class L2T(l: Long) {
    def asTimestamp: Timestamp = Timestamp.from(Instant.ofEpochMilli(l))
  }

  implicit class L2S(l: Long) {

    def asTimeString: String = this.asTimeStringWithOffset()

    def asTimeStringWithOffset(offset: Int = 8): String =
      Instant.ofEpochMilli(l)
        .atOffset(ZoneOffset.ofHours(8))
        .format(DateTimeFormatter.ISO_DATE_TIME)

  }

  implicit class I2S(i: Instant) {

    def asInstantString: String = asInstantStringWithOffset()

    def asInstantStringWithOffset(offset: Int = 8): String = {
      i
        .atOffset(ZoneOffset.ofHours(8))
        .format(DateTimeFormatter.ISO_DATE_TIME)
    }
  }

}
