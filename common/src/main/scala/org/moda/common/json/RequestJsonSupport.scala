package org.moda.common.json

import akka.http.scaladsl.model.ContentTypeRange
import akka.http.scaladsl.model.MediaTypes.`application/json`
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, Unmarshaller}
import akka.util.ByteString

import scala.collection.immutable.Seq
import scala.reflect.runtime.universe._

trait RequestJsonSupport {
  def unmarshallerContentTypes: Seq[ContentTypeRange] =
    List(`application/json`)

  private val jsonStringUnmarshaller =
      Unmarshaller.byteStringUnmarshaller
      .forContentTypes(unmarshallerContentTypes: _*)
      .mapWithCharset {
        case (ByteString.empty, _) => throw Unmarshaller.NoContentException
        case (data, charset)       => data.decodeString(charset.nioCharset.name)
      }

  /**
   * HTTP entity => `A`
   */
  implicit def unmarshaller[A](implicit ct: TypeTag[A],
                               objectMapper: ObjectMapper = defaultObjectMapper
                              ): FromEntityUnmarshaller[A] =
    jsonStringUnmarshaller.map(
      data => objectMapper.readValue(data, typeReference[A]).asInstanceOf[A]
    )
}
