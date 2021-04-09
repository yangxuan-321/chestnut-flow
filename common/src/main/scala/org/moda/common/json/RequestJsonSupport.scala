package org.moda.common.json

import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.unmarshalling.Unmarshaller
import spray.json.JsObject

trait RequestJsonSupport {
  /**
   * HTTP entity => `A`
   */
//  implicit val um: Unmarshaller[HttpEntity, JsObject] = {
//    Unmarshaller.byteStringUnmarshaller.mapWithCharset { (data,charset) =>
//      decode[JsObject]
//    }
//  }
}
