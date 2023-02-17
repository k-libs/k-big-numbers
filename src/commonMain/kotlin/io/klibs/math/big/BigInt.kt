package io.klibs.math.big

sealed interface BigInt {

  /**
   * `true` if this [BigInt] is equal to `0`.
   */
  val isZero: Boolean

  /**
   * `true` if this [BigInt] represents a negative, non-zero number.
   */
  val isNegative: Boolean

  /**
   * `true` if this [BigInt] represents a positive, non-zero number.
   */
  val isPositive: Boolean

  /**
   * Tests whether the value contained within this [BigInt] could safely fit
   * type [Byte] without overflow.
   *
   * @return `true` if this `BigInt` falls in the range [Byte.MIN_VALUE] ..
   * [Byte.MAX_VALUE], otherwise `false`.
   */
  fun fitsByte(): Boolean

  /**
   * Unwraps the value contained within this [BigInt] as a [Byte] value.
   *
   * If this `BigInt` value is greater than [Byte.MAX_VALUE] or is less than
   * [Byte.MIN_VALUE], an exception will be thrown.
   *
   * @return The value contained within this `BigInt` as a `Byte` value.
   */
  fun toByte(): Byte

  /**
   * Tests whether the value contained within this [BigInt] could safely fit
   * type [Short] without overflow.
   *
   * @return `true` if this `BigInt` falls in the range [Short.MIN_VALUE] ..
   * [Short.MAX_VALUE], otherwise `false`.
   */
  fun fitsShort(): Boolean

  /**
   * Unwraps the value contained within this [BigInt] as a [Short] value.
   *
   * If this `BigInt` value is greater than [Short.MAX_VALUE] or is less than
   * [Short.MIN_VALUE], an exception will be thrown.
   *
   * @return The value contained within this `BigInt` as a `Short` value.
   */
  fun toShort(): Short

  /**
   * Tests whether the value contained within this [BigInt] could safely fit
   * type [Int] without overflow.
   *
   * @return `true` if this `BigInt` falls in the range [Int.MIN_VALUE] ..
   * [Int.MAX_VALUE], otherwise `false`.
   */
  fun fitsInt(): Boolean

  /**
   * Unwraps the value contained within this [BigInt] as a [Int] value.
   *
   * If this `BigInt` value is greater than [Int.MAX_VALUE] or is less than
   * [Int.MIN_VALUE], an exception will be thrown.
   *
   * @return The value contained within this `BigInt` as a `Int` value.
   */
  fun toInt(): Int

  /**
   * Tests whether the value contained within this [BigInt] could safely fit
   * type [Long] without overflow.
   *
   * @return `true` if this `BigInt` falls in the range [Long.MIN_VALUE] ..
   * [Long.MAX_VALUE], otherwise `false`.
   */
  fun fitsLong(): Boolean

  /**
   * Unwraps the value contained within this [BigInt] as a [Long] value.
   *
   * If this `BigInt` value is greater than [Long.MAX_VALUE] or is less than
   * [Long.MIN_VALUE], an exception will be thrown.
   *
   * @return The value contained within this `BigInt` as a `Long` value.
   */
  fun toLong(): Long

  fun fitsUByte(): Boolean
  fun fitsUShort(): Boolean
  fun fitsUInt(): Boolean
  fun fitsULong(): Boolean

  fun toUByte(): UByte
  fun toUShort(): UShort
  fun toUInt(): UInt
  fun toULong(): ULong

  /**
   * Returns a new [BigInt] wrapping the sum of the value of this `BigInt` and
   * the value of [rhs].
   *
   * @param rhs Other `BigInt` that will be added to this `BigInt` to produce
   * the returned sum value.
   *
   * @return The sum of this `BigInt` and the given [rhs] `BigInt`.
   */
  operator fun plus(rhs: BigInt): BigInt

  operator fun minus(rhs: BigInt): BigInt

  operator fun unaryMinus(): BigInt

  operator fun times(rhs: BigInt): BigInt

  operator fun div(rhs: BigInt): BigInt

  operator fun rem(rhs: BigInt): BigInt

  fun divideAndRemainder(value: BigInt): Pair<BigInt, BigInt>

  infix fun shr(n: Int): BigInt

  infix fun shl(n: Int): BigInt

  infix fun and(value: BigInt): BigInt

  infix fun or(value: BigInt): BigInt

  infix fun xor(value: BigInt): BigInt

  infix fun andNot(value: BigInt): BigInt

  infix fun pow(exponent: Int): BigInt

  fun inv(): BigInt

  fun abs(): BigInt

  fun square(): BigInt

  fun bitLength(): Int

  fun bitCount(): Int

  fun getLowestSetBit(): Int

  fun toString(radix: Int): String

  companion object {
    val NegativeOne: BigInt = BigIntImpl(BYTE_NEG_ONE, intArrayOf(1))

    val Zero: BigInt = BigIntImpl(BYTE_ZERO, IntArray(0))

    val One: BigInt = BigIntImpl(BYTE_ONE, intArrayOf(1))

    val Ten: BigInt = BigIntImpl(BYTE_ONE, intArrayOf(10))
  }
}