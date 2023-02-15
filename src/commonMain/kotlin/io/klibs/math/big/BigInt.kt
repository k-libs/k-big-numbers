package io.klibs.math.big

sealed interface BigInt {
  val isZero: Boolean
  val isNegative: Boolean
  val isPositive: Boolean

  operator fun plus(rhs: BigInt): BigInt

  operator fun minus(rhs: BigInt): BigInt

  operator fun unaryMinus(): BigInt

  operator fun times(rhs: BigInt): BigInt

  infix fun shr(n: Int): BigInt

  infix fun shl(n: Int): BigInt

  infix fun and(value: BigInt): BigInt

  infix fun or(value: BigInt): BigInt

  infix fun xor(value: BigInt): BigInt

  infix fun andNot(value: BigInt): BigInt

  fun inv(): BigInt

  fun abs(): BigInt

  fun square(): BigInt

  fun bitLength(): Int

  fun bitCount(): Int

  companion object {
    val NegativeOne: BigInt = BigIntImpl(BYTE_NEG_ONE, intArrayOf(1))

    val Zero: BigInt = BigIntImpl(BYTE_ZERO, IntArray(0))

    val One: BigInt = BigIntImpl(BYTE_ONE, intArrayOf(1))

    val Ten: BigInt = BigIntImpl(BYTE_ONE, intArrayOf(10))
  }
}