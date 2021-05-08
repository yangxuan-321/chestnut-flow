package org.moda.auth.api

import akka.http.scaladsl.server.Route
import org.moda.idl.SimpleAuthUser

/**
 * @author moda-master
 * @date 2020/9/4 下午4:32
 */
trait Api {
//trait Api {
//  val yyMMddHHmmssFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//
//  implicit def encoderEnum[A <: scalapb.GeneratedEnum]: Encoder[A] = (x: A) => Json.fromInt(x.value)
//
//  implicit def decoderEnum[A <: scalapb.GeneratedEnum](f: Int => A): Decoder[A] =
//    Decoder.decodeOption[Int].emap {
//      case Some(x) => Right(f(x))
//      case _ => Right(f(0))
//    }


  def publicR: Route

  def authedR: SimpleAuthUser => Route
}
