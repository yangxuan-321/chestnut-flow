package org.moda.core.api

import akka.http.scaladsl.server.Route
import org.moda.common.json.{PbJsonSupport, RequestJsonSupport}

/**
 * @author moda-master
 * @date 2020/9/4 下午4:32
 */
trait Api extends PbJsonSupport
  with RequestJsonSupport {

  def routes: Route

}

