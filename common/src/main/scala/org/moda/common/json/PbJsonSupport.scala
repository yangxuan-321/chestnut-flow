package org.moda.common.json

import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date

import io.circe.{Decoder, Encoder, Json}

object PbJsonExtendSupport extends PbJsonEnumSupport

trait PbJsonEnumSupport {

  implicit def encoderEnum[A <: scalapb.GeneratedEnum]: Encoder[A] = (x: A) => Json.fromInt(x.value)

  implicit def decoderEnum[A <: scalapb.GeneratedEnum](f: Int => A): Decoder[A] =
    Decoder.decodeOption[Int].emap {
      case Some(x) => Right(f(x))
      case _ => Right(f(0))
    }

}

trait PbJsonInstantSupport {
  val yyMMddHHmmssFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  implicit def encoderInstant[B <: Instant]: Encoder[B] = (x: B) =>
    Json.fromString(yyMMddHHmmssFormat.format(new Date(x.toEpochMilli)))
}