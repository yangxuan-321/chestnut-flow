package org.moda.common.json

import io.circe.Encoder
import org.moda.common.model.Pretty
import io.circe.syntax._
import io.circe.generic.auto._

object Json2String{
  implicit class Json2String[T](ct: Pretty[T]) {
    implicit def toJsonString()(implicit f: Encoder[T]): String = {
      ct.asJson.toString
    }
  }
}
