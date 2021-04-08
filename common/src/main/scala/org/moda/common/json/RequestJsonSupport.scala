package org.moda.common.json

import akka.http.scaladsl.unmarshalling.FromEntityUnmarshaller

trait RequestJsonSupport {
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
