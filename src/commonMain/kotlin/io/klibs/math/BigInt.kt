package io.klibs.math

interface BigInt {
  operator fun plus(lhs: Byte): BigInt
  operator fun plus(lhs: Short): BigInt
  operator fun plus(lhs: Int): BigInt
  operator fun plus(lhs: Long): BigInt
  operator fun plus(lhs: UByte): BigInt
  operator fun plus(lhs: UShort): BigInt
  operator fun plus(lhs: UInt): BigInt
  operator fun plus(lhs: ULong): BigInt
  operator fun plus(lhs: BigInt): BigInt

  operator fun minus(lhs: Byte): BigInt
  operator fun minus(lhs: Short): BigInt
  operator fun minus(lhs: Int): BigInt
  operator fun minus(lhs: Long): BigInt
  operator fun minus(lhs: UByte): BigInt
  operator fun minus(lhs: UShort): BigInt
  operator fun minus(lhs: UInt): BigInt
  operator fun minus(lhs: ULong): BigInt
  operator fun minus(lhs: BigInt): BigInt

  operator fun times(lhs: Byte): BigInt
  operator fun times(lhs: Short): BigInt
  operator fun times(lhs: Int): BigInt
  operator fun times(lhs: Long): BigInt
  operator fun times(lhs: UByte): BigInt
  operator fun times(lhs: UShort): BigInt
  operator fun times(lhs: UInt): BigInt
  operator fun times(lhs: ULong): BigInt
  operator fun times(lhs: BigInt): BigInt

  operator fun div(lhs: Byte): BigInt
  operator fun div(lhs: Short): BigInt
  operator fun div(lhs: Int): BigInt
  operator fun div(lhs: Long): BigInt
  operator fun div(lhs: UByte): BigInt
  operator fun div(lhs: UShort): BigInt
  operator fun div(lhs: UInt): BigInt
  operator fun div(lhs: ULong): BigInt
  operator fun div(lhs: BigInt): BigInt

  fun toPlainString(): String
}

