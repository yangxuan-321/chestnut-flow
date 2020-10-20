//package org.moda.common.http
//
//import akka.http.scaladsl.marshalling.ToEntityMarshaller
//import akka.http.scaladsl.model.MediaTypes
//import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, Unmarshaller}
//
//import scala.reflect.ClassTag
//
//trait JacksonSupport {
//
//  private val jsonStringUnmarshaller =
//    Unmarshaller.byteStringUnmarshaller
//      .forContentTypes(MediaTypes.`application/json`)
//      .mapWithCharset {
//        case (ByteString.empty, _) => throw Unmarshaller.NoContentException
//        case (data, charset) => data.decodeString(charset.nioCharset.name)
//      }
//
//  // HTTP entity => `A`
//  implicit def unmarshaller[A](
//                                implicit ct: ClassTag[A],
//                                objectMapper: ObjectMapper = Jackson.defaultObjectMapper
//                              ): FromEntityUnmarshaller[A] =
//    jsonStringUnmarshaller.map(
//      data => objectMapper.readValue(data, ct.runtimeClass).asInstanceOf[A]
//    )
//
//  // `A` => HTTP entity
//  implicit def marshaller[A](
//                              implicit objectMapper: ObjectMapper = Jackson.defaultObjectMapper
//                            ): ToEntityMarshaller[A] = {
//    JacksonHelper.marshaller[A](objectMapper)
//  }
//
//}