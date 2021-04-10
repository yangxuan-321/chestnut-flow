package org.moda.auth.middleware

import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.server.Directives.{complete, optionalHeaderValueByName}
import org.moda.idl.{AuthUser, SimpleAuthUser}

object TokenAuthenticate {

}

trait TokenAuthenticate {
  private val headerName = "authorization"

  def authenticate: Directive1[SimpleAuthUser] = {
    optionalHeaderValueByName(headerName).flatMap { x =>
      x.getOrElse("")
    }
  }

  private[this] def parseUserInfo(token: String): Directive1[SimpleAuthUser] = {
    token match {
      case "" => complete(ApiError(status = ApiStatus.UnKnowError, Some(s"token is empty")))
    }
  }
}
