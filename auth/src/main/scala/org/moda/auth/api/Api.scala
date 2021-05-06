package org.moda.auth.api

import akka.http.scaladsl.server.Route
import org.moda.idl.SimpleAuthUser

/**
 * @author moda-master
 * @date 2020/9/4 下午4:32
 */
//trait Api extends PbJsonSupport {
trait Api {
  def publicR: Route

  def authedR: SimpleAuthUser => Route
}
