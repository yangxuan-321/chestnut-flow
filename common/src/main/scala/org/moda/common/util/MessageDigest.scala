package org.moda.common.util

import java.nio.charset.StandardCharsets

import com.google.common.hash.Hashing


/**
  * Created by weiwen on 17-6-13.
  */

@com.github.ghik.silencer.silent
object MessageDigest {

  private[this] val c = StandardCharsets.UTF_8

  def sha1AsHex(s: String): String = Hashing.sha1().hashString(s, c).toString

  def sha256AsHex(s: String): String = Hashing.sha256().hashString(s, c).toString

  def sha384AsHex(s: String): String = Hashing.sha384().hashString(s, c).toString

  def sha512AsHex(s: String): String = Hashing.sha512().hashString(s, c).toString

  def md5AsHex(s: String): String = Hashing.md5().hashString(s, c).toString

  def sha1AsHex(bytes: Array[Byte]): String = Hashing.sha1().hashBytes(bytes).toString

  def sha256AsHex(bytes: Array[Byte]): String = Hashing.sha256().hashBytes(bytes).toString

  def sha384AsHex(bytes: Array[Byte]): String = Hashing.sha384().hashBytes(bytes).toString

  def sha512AsHex(bytes: Array[Byte]): String = Hashing.sha512().hashBytes(bytes).toString

  def md5AsHex(bytes: Array[Byte]): String = Hashing.md5().hashBytes(bytes).toString

  def sha1(bytes: Array[Byte]): Array[Byte] = Hashing.sha1().hashBytes(bytes).asBytes

  def sha256(bytes: Array[Byte]): Array[Byte] = Hashing.sha256().hashBytes(bytes).asBytes

  def sha384(bytes: Array[Byte]): Array[Byte] = Hashing.sha384().hashBytes(bytes).asBytes

  def sha512(bytes: Array[Byte]): Array[Byte] = Hashing.sha512().hashBytes(bytes).asBytes

  def md5(bytes: Array[Byte]): Array[Byte] = Hashing.md5().hashBytes(bytes).asBytes

}
