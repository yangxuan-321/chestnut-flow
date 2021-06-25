package org.moda.common.util

object UUID {
  def uuid(): String = {
    java.util.UUID.randomUUID().toString.take(32).toUpperCase
  }
}
