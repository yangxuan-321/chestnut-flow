//package org.moda.common.json
//
//import akka.http.scaladsl.model.ContentTypeRange
//import akka.http.scaladsl.model.MediaTypes.`application/json`
//import akka.http.scaladsl.unmarshalling.Unmarshaller
//import akka.util.ByteString
//
//import scala.collection.immutable.Seq
//
//trait RequestJsonSupport {
//  def unmarshallerContentTypes: Seq[ContentTypeRange] =
//    List(`application/json`)
//
//  implicit val jsonStringUnmarshaller =
//      Unmarshaller.byteStringUnmarshaller
//      .forContentTypes(unmarshallerContentTypes: _*)
//      .mapWithCharset {
//        case (ByteString.empty, _) => throw Unmarshaller.NoContentException
//        case (data, charset)       => data.decodeString(charset.nioCharset.name)
//      }
//
//  /**
//   * HTTP entity => `A`
//   */
////  implicit val um: Unmarshaller[HttpEntity, JsObject] = {
////    Unmarshaller.byteStringUnmarshaller.mapWithCharset { (data,charset) =>
////      decode[JsObject]
////    }
////  }
//}
