package org.moda.auth

import io.circe.generic.auto._
import io.circe.{Decoder, Encoder, Json}

package object api {

  trait ApiEnum extends Any with Product with Serializable {
    type EnumType <: ApiEnum
    def value: Int
    def name: String
    override def toString: String = name
    def companion: ApiEnumCompanion[EnumType]
  }

  trait ApiEnumCompanion[A <: ApiEnum] {
    type ValueType = A
    def fromValue(value: Int): A
    def fromName(name: String): Option[A] = nMap.get(name)
    def values: Seq[A]
    lazy val vMap: Map[Int, A]    = values.map(x => (x.value, x)).toMap
    lazy val nMap: Map[String, A] = values.map(x => (x.name, x)).toMap
  }

  sealed abstract class ApiStatus(val value: Int, val name: String) extends ApiEnum {
    type EnumType = ApiStatus
    def companion: ApiEnumCompanion[ApiStatus] = ApiStatus
  }

  object ApiStatus extends ApiEnumCompanion[ApiStatus] {

    implicit def enumCompanion: ApiEnumCompanion[ApiStatus] = this

    override def fromValue(value: Int): ApiStatus = vMap.getOrElse(value, UnKnownError)

    override def fromName(name: String): Option[ApiStatus] = nMap.get(name)

    final case object UnKnownError        extends ApiStatus(-1, "UnknownError")
    final case object Ok                  extends ApiStatus(0, "Ok")
    final case object ParameterError      extends ApiStatus(1, "ParameterError")
    final case object Forbidden           extends ApiStatus(403, "Forbidden")
    final case object TokenInvalid        extends ApiStatus(7, "TokenInvalid")
    final case object TokenExpired        extends ApiStatus(9, "TokenExpired")

    final case object InternalServerError extends ApiStatus(500, "服务器错误")

    val values: List[ApiStatus] =
      List(
        UnKnownError,
        Ok,
        Forbidden,
        InternalServerError,
        TokenInvalid,
        TokenExpired
      )

  }

  sealed trait ApiData[+T] {
    def status: ApiStatus
  }

  final case class Pretty[+T](data: T, status: ApiStatus) extends ApiData[T]

  object Pretty {
    def apply[T](data: T): Pretty[T] = Pretty(data, ApiStatus.Ok)
  }

  final case class ApiError(status: ApiStatus, message: Option[String]) extends ApiData[Nothing]

  object ApiError {

    def apply(status: ApiStatus, message: String): ApiError = ApiError(status, Some(message))

    def apply(status: ApiStatus): ApiError = ApiError(status, None)

    def tokenExpired: ApiError = ApiError(status = ApiStatus.TokenExpired)

    def tokenInvalid: ApiError = ApiError(status = ApiStatus.TokenInvalid)

  }

  implicit def apiStatusEncoder[A <: ApiStatus]: Encoder[A] =
    (x: A) => Json.fromInt(x.value)

  implicit def apiStatusDecoder[A <: ApiStatus]: Decoder[A] =
    Decoder.decodeInt.map(ApiStatus.fromValue).map(_.asInstanceOf[A])

  implicit def apiDataEncoder[A: Encoder]: Encoder[ApiData[A]] = {
    case y: Pretty[A] => implicitly[Encoder[Pretty[A]]].apply(y)
    case y: ApiError  => implicitly[Encoder[ApiError]].apply(y)
  }

}
