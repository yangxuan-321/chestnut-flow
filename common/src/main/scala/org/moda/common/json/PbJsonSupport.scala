package org.moda.common.json

import io.circe.{Decoder, Encoder, Json}

trait PbJsonSupport {

  def encoder[A <: scalapb.GeneratedEnum]: Encoder[A] = (x: A) => Json.fromInt(x.value)

  def decoder[A <: scalapb.GeneratedEnum](f: Int => A): Decoder[A] =
    Decoder.decodeOption[Int].emap {
      case Some(x) => Right(f(x))
      case _ => Right(f(0))
    }

  def decoder[A](implicit d: Decoder[A]): Decoder[Seq[A]] =
    Decoder.decodeOption[List[A]].emap {
      case Some(x) => Right(x)
      case _ => Right(List.empty[A])
    }

}
