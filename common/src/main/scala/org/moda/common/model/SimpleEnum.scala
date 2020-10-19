package org.moda.common.model

trait SimpleEnum extends Any with Product with Serializable {
  type EnumType <: SimpleEnum
  def value: Int
  def name: String
  def companion: SimpleEnumCompanion[EnumType]
  override def toString: String = name
}

trait SimpleEnumCompanion[A <: SimpleEnum] {
  type ValueType = A
  def default: A
  protected def values: Vector[A]
  implicit def companion: SimpleEnumCompanion[A] = this
  lazy val members: Vector[A]                    = values.sortBy(_.value)
  def fromValue(value: Int): A                   = vMap.getOrElse(value, default)
  def fromName(name: String): Option[A]          = nMap.get(name)
  protected lazy val vMap: Map[Int, A]           = values.map(x => x.value -> x).toMap
  protected lazy val nMap: Map[String, A]        = values.map(x => x.name -> x).toMap
}

sealed abstract class SimpleStatus(val value: Int, val name: String) extends SimpleEnum {
  type EnumType = SimpleStatus
  def companion: SimpleEnumCompanion[SimpleStatus] = SimpleStatus
}

object SimpleStatus extends SimpleEnumCompanion[SimpleStatus] {
  case object Default  extends SimpleStatus(0, "Default")
  case object Active   extends SimpleStatus(1, "Active")
  case object InActive extends SimpleStatus(2, "InActive")
  val default: SimpleStatus        = Default
  val values: Vector[SimpleStatus] = Vector(Default, Active, InActive)
}
