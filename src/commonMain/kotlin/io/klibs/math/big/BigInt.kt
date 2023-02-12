package io.klibs.math.big

import io.klibs.collections.uintDequeOf

sealed interface BigInt {
  val isNegative: Boolean
  val isPositive: Boolean
  val isZero: Boolean

//  operator fun plus(rhs: BigInt): BigInt

//  operator fun minus(rhs: BigInt): BigInt

//  operator fun times(rhs: BigInt): BigInt

//  operator fun compareTo(rhs: BigInt): Int

  fun abs(): BigInt

  operator fun unaryMinus(): BigInt

  fun toString(radix: BigIntRadix): String

  companion object {
    @OptIn(ExperimentalUnsignedTypes::class)
    val Zero: BigInt = ImmutableBigInt(MutableBigIntImpl(0, uintDequeOf()))

    @OptIn(ExperimentalUnsignedTypes::class)
    val One: BigInt = ImmutableBigInt(MutableBigIntImpl(1, uintDequeOf(1u)))

    inline fun ofByte(b: Byte) = bigIntOf(b)
    inline fun ofShort(s: Short) = bigIntOf(s)
    inline fun ofInt(i: Int) = bigIntOf(i)
    inline fun ofLong(l: Long) = bigIntOf(l)

    inline fun ofUByte(u: UByte) = bigIntOf(u)
    inline fun ofUShort(u: UShort) = bigIntOf(u)
    inline fun ofUInt(u: UInt) = bigIntOf(u)
    inline fun ofULong(u: ULong) = bigIntOf(u)
  }
}

