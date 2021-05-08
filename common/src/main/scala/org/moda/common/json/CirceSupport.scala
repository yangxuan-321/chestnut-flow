package org.moda.common.json

import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date

import akka.http.scaladsl.marshalling.{Marshaller, ToEntityMarshaller}
import akka.http.scaladsl.model.MediaTypes.`application/json`
import akka.http.scaladsl.model.{ContentTypeRange, HttpEntity}
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, Unmarshaller}
import akka.util.ByteString
import cats.data.NonEmptyList
import cats.syntax.show._
import io.circe._

import scala.collection.immutable.Seq


/**
  * Automatic to and from JSON marshalling/unmarshalling using an in-scope circe Protocol.
  * The unmarshaller fails fast, throwing the first `Error` encountered.
  *
  * To use automatic codec derivation, user needs to import `io.circe.generic.auto._`.
  */
object FailFastCirceSupport extends FailFastCirceSupport

/**
  * Automatic to and from JSON marshalling/unmarshalling using an in-scope circe Protocol.
  * The unmarshaller fails fast, throwing the first `Error` encountered.
  *
  * To use automatic codec derivation import `io.circe.generic.auto._`.
  */
trait FailFastCirceSupport extends BaseCirceSupport with FailFastUnmarshaller

/**
  * Automatic to and from JSON marshalling/unmarshalling using an in-scope circe Protocol.
  * The unmarshaller accumulates all errors in the exception `Errors`.
  *
  * To use automatic codec derivation, user needs to import `io.circe.generic.auto._`.
  */
object ErrorAccumulatingCirceSupport extends ErrorAccumulatingCirceSupport {
  final case class DecodingFailures(failures: NonEmptyList[DecodingFailure]) extends Exception {
    override def getMessage = failures.toList.map(_.show).mkString("\n")
  }
}

/**
  * Automatic to and from JSON marshalling/unmarshalling using an in-scope circe Protocol.
  * The unmarshaller accumulates all errors in the exception `Errors`.
  *
  * To use automatic codec derivation import `io.circe.generic.auto._`.
  */
trait ErrorAccumulatingCirceSupport extends BaseCirceSupport with ErrorAccumulatingUnmarshaller

/**
  * Automatic to and from JSON marshalling/unmarshalling using an in-scope circe Protocol.
  */
trait BaseCirceSupport {
  // val m = MediaType.customWithFixedCharset("application", "json;charset=utf-8", HttpCharsets.`UTF-8`)
  def unmarshallerContentTypes: Seq[ContentTypeRange] =
    List(`application/json`)

  /**
    * `Json` => HTTP entity
    *
    * @return marshaller for JSON value
    */
  implicit def jsonMarshaller(
      implicit printer: Printer = Printer.noSpaces
  ): ToEntityMarshaller[Json] =
    Marshaller.withFixedContentType(`application/json`) { json =>
      HttpEntity(`application/json`, printer.pretty(json))
    }

  /**
    * `A` => HTTP entity
    *
    * @tparam A type to encode
    * @return marshaller for any `A` value
    */
  implicit final def marshaller[A: Encoder](
      implicit printer: Printer = Printer.noSpaces
  ): ToEntityMarshaller[A] =
    jsonMarshaller(printer).compose(implicitly[Encoder[A]].apply)

  /**
    * HTTP entity => `Json`
    *
    * @return unmarshaller for `Json`
    */
  implicit final val jsonUnmarshaller: FromEntityUnmarshaller[Json] =
    Unmarshaller.byteStringUnmarshaller
      .forContentTypes(unmarshallerContentTypes: _*)
      .map {
        case ByteString.empty => throw Unmarshaller.NoContentException
        case data             => jawn.parseByteBuffer(data.asByteBuffer).fold(throw _, identity)
      }

  /**
    * HTTP entity => `A`
    *
    * @tparam A type to decode
    * @return unmarshaller for `A`
    */
  implicit def unmarshaller[A: Decoder]: FromEntityUnmarshaller[A]

  val yyMMddHHmmssFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  implicit def encoderInstant[A <: Instant]: Encoder[A] = (x: A) => Json.fromString(yyMMddHHmmssFormat.format(new Date(x.toEpochMilli)))

  implicit def encoderEnum[A <: scalapb.GeneratedEnum]: Encoder[A] = (x: A) => Json.fromInt(x.value)

  implicit def decoderEnum[A <: scalapb.GeneratedEnum](f: Int => A): Decoder[A] =
    Decoder.decodeOption[Int].emap {
      case Some(x) => Right(f(x))
      case _ => Right(f(0))
    }

}

/**
  * Mix-in this trait to fail on the first error during unmarshalling.
  */
trait FailFastUnmarshaller { this: BaseCirceSupport =>

  override implicit final def unmarshaller[A: Decoder]: FromEntityUnmarshaller[A] = {
    def decode(json: Json) = implicitly[Decoder[A]].decodeJson(json).fold(throw _, identity)
    jsonUnmarshaller.map(decode)
  }
}

/**
  * Mix-in this trait to accumulate all errors during unmarshalling.
  */
trait ErrorAccumulatingUnmarshaller { this: BaseCirceSupport =>

  override implicit final def unmarshaller[A: Decoder]: FromEntityUnmarshaller[A] = {
    def decode(json: Json) =
      implicitly[Decoder[A]]
        .accumulating(json.hcursor)
        .fold(failures => throw ErrorAccumulatingCirceSupport.DecodingFailures(failures), identity)
    jsonUnmarshaller.map(decode)
  }
}