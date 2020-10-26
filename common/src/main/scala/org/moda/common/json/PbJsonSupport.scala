package org.moda.common.json

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Date

import io.circe.{Decoder, Encoder, Json}

trait PbJsonSupport {

  val yyMMddHHmmssFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  implicit def encoder[A <: scalapb.GeneratedEnum]: Encoder[A] = (x: A) => Json.fromInt(x.value)

  implicit def encoderInstant[A <: Instant]: Encoder[A] = (x: A) => Json.fromString(yyMMddHHmmssFormat.format(new Date(x.toEpochMilli)))

  implicit def decoder[A <: scalapb.GeneratedEnum](f: Int => A): Decoder[A] =
    Decoder.decodeOption[Int].emap {
      case Some(x) => Right(f(x))
      case _ => Right(f(0))
    }

  implicit def decoder[A](implicit d: Decoder[A]): Decoder[Seq[A]] =
    Decoder.decodeOption[List[A]].emap {
      case Some(x) => Right(x)
      case _ => Right(List.empty[A])
    }

}
