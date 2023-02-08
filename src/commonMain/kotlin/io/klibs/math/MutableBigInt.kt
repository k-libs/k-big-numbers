package io.klibs.math

sealed interface MutableBigInt : BigInt {
  operator fun inc(): MutableBigInt
  operator fun dec(): MutableBigInt

  operator fun plusAssign(lhs: Byte)
  operator fun plusAssign(lhs: Short)
  operator fun plusAssign(lhs: Int)
  operator fun plusAssign(lhs: Long)
  operator fun plusAssign(lhs: UByte)
  operator fun plusAssign(lhs: UShort)
  operator fun plusAssign(lhs: UInt)
  operator fun plusAssign(lhs: ULong)
  operator fun plusAssign(lhs: BigInt)

  operator fun minusAssign(lhs: Byte)
  operator fun minusAssign(lhs: Short)
  operator fun minusAssign(lhs: Int)
  operator fun minusAssign(lhs: Long)
  operator fun minusAssign(lhs: UByte)
  operator fun minusAssign(lhs: UShort)
  operator fun minusAssign(lhs: UInt)
  operator fun minusAssign(lhs: ULong)
  operator fun minusAssign(lhs: BigInt)

  operator fun timesAssign(lhs: Byte)
  operator fun timesAssign(lhs: Short)
  operator fun timesAssign(lhs: Int)
  operator fun timesAssign(lhs: Long)
  operator fun timesAssign(lhs: UByte)
  operator fun timesAssign(lhs: UShort)
  operator fun timesAssign(lhs: UInt)
  operator fun timesAssign(lhs: ULong)
  operator fun timesAssign(lhs: BigInt)

  operator fun divAssign(lhs: Byte)
  operator fun divAssign(lhs: Short)
  operator fun divAssign(lhs: Int)
  operator fun divAssign(lhs: Long)
  operator fun divAssign(lhs: UByte)
  operator fun divAssign(lhs: UShort)
  operator fun divAssign(lhs: UInt)
  operator fun divAssign(lhs: ULong)
  operator fun divAssign(lhs: BigInt)

  operator fun remAssign(lhs: Byte)
  operator fun remAssign(lhs: Short)
  operator fun remAssign(lhs: Int)
  operator fun remAssign(lhs: Long)
  operator fun remAssign(lhs: UByte)
  operator fun remAssign(lhs: UShort)
  operator fun remAssign(lhs: UInt)
  operator fun remAssign(lhs: ULong)
  operator fun remAssign(lhs: BigInt)
}