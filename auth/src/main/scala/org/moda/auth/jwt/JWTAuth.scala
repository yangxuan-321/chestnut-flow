package org.moda.auth.jwt

import java.time.Instant

import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}

import io.circe.syntax._
import io.circe.{Decoder, Encoder}
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}
import pdi.jwt.algorithms.JwtHmacAlgorithm
import pdi.jwt.exceptions.JwtExpirationException


object JWTAuth {

  def default: JWTAuth = new JWTAuth {}

  sealed trait JWTAuthResult

  final case object JwtInvalid extends JWTAuthResult

  final case object JwtExpired extends JWTAuthResult

}


trait JWTAuth {

  import JWTAuth._

  val algorithm: JwtHmacAlgorithm = JwtAlgorithm.HS256

  def encode[T : ClassTag](underlying: T, expireMs: Long = 3600000L)(key: String)(implicit encoder: Encoder[T]): String = {
    val content: String = underlying.asJson.noSpaces
    val claim = JwtClaim(
      content = content
      , expiration = Some(Instant.now.plusMillis(expireMs).getEpochSecond)
      , issuedAt = Some(Instant.now.getEpochSecond)
    )
    val token: String = JwtCirce.encode(claim, key, algorithm)
    token
  }

  def decode[T : ClassTag](token: String)(key: String)(implicit decoder: Decoder[T]): Either[JWTAuthResult, T] = {
    val r: Either[JWTAuthResult, T] = JwtCirce.decode(token, key, Seq(algorithm)) match {
      case Success(c) => io.circe.parser.decode[T](c.content) match {
        case Right(x: T) => Right(x)
        case Left(_)  => Left(JwtInvalid)
      }
      case Failure(_: JwtExpirationException) => Left(JwtExpired)
      case Failure(_) => Left(JwtInvalid)
    }
    r
  }
}
