//package org.moda.common.http
//
//import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
//import spray.json.DefaultJsonProtocol
//
//final case class ResultVO[T](status: Int, message: String, data: T)
//
//trait JsonSupport[T] extends SprayJsonSupport with DefaultJsonProtocol {
//  implicit val resultFormat = jsonFormat2(ResultVO[T])
//}