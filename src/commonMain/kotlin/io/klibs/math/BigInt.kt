package io.klibs.math

import io.klibs.collections.byteDequeOf

sealed interface BigInt {

  /**
   * Indicates whether this `BigInt` represents a negative value.
   *
   * If this `BigInt` is equal to `0`, then this will be `false`.
   */
  val isNegative: Boolean

  /**
   * Indicates whether this `BigInt` represents a positive value.
   *
   * If this `BigInt` is equal to `0`, then this will be `false`.
   */
  val isPositive: Boolean

  /**
   * Indicates whether this `BigInt` is equal to `0`.
   */
  val isZero: Boolean

  /**
   * Tests whether the value of this [BigInt] could fit into a [Byte] without
   * overflowing it.
   *
   * Essentially testing `-128 <= this <= 127`
   *
   * @return `true` if the value of this `BigInt` could fit into a `Byte` type
   * without overflowing it.  `false` if the value of this `BigInt` would
   * overflow a `Byte`.
   */
  fun fitsByte(): Boolean

  /**
   * Tests whether the value of this [BigInt] could fit into a [Short] without
   * overflowing it.
   *
   * Essentially testing `-32768 <= this <= 32767`
   *
   * @return `true` if the value of this `BigInt` could fit into a `Short` type
   * without overflowing it.  `false` if the value of this `BigInt` would
   * overflow a `Short`.
   */
  fun fitsShort(): Boolean

  /**
   * Tests whether the value of this [BigInt] could fit into a [Int] without
   * overflowing it.
   *
   * Essentially testing `-2147483648 <= this <= 2147483647`
   *
   * @return `true` if the value of this `BigInt` could fit into a `Int` type
   * without overflowing it.  `false` if the value of this `BigInt` would
   * overflow a `Int`.
   */
  fun fitsInt(): Boolean

  /**
   * Tests whether the value of this [BigInt] could fit into a [Long] without
   * overflowing it.
   *
   * Essentially testing `-9223372036854775808 <= this <= 9223372036854775807`
   *
   * @return `true` if the value of this `BigInt` could fit into a `Long` type
   * without overflowing it.  `false` if the value of this `BigInt` would
   * overflow a `Long`.
   */
  fun fitsLong(): Boolean

  /**
   * Tests whether the value of this [BigInt] could fit into a [UByte] without
   * overflowing it.
   *
   * Essentially testing `0 <= this <= 255`
   *
   * @return `true` if the value of this `BigInt` could fit into a `UByte` type
   * without overflowing it.  `false` if the value of this `BigInt` would
   * overflow a `UByte`.
   */
  fun fitsUByte(): Boolean

  /**
   * Tests whether the value of this [BigInt] could fit into a [UShort] without
   * overflowing it.
   *
   * Essentially testing `0 <= this <= 65535`
   *
   * @return `true` if the value of this `BigInt` could fit into a `UShort` type
   * without overflowing it.  `false` if the value of this `BigInt` would
   * overflow a `UShort`.
   */
  fun fitsUShort(): Boolean

  /**
   * Tests whether the value of this [BigInt] could fit into a [UInt] without
   * overflowing it.
   *
   * Essentially testing `0 <= this <= 4294967295`
   *
   * @return `true` if the value of this `BigInt` could fit into a `UInt` type
   * without overflowing it.  `false` if the value of this `BigInt` would
   * overflow a `UInt`.
   */
  fun fitsUInt(): Boolean

  /**
   * Tests whether the value of this [BigInt] could fit into a [ULong] without
   * overflowing it.
   *
   * Essentially testing `0 <= this <= 18446744073709551615`
   *
   * @return `true` if the value of this `BigInt` could fit into a `ULong` type
   * without overflowing it.  `false` if the value of this `BigInt` would
   * overflow a `ULong`.
   */
  fun fitsULong(): Boolean

  fun toByte(): Byte
  fun toShort(): Short
  fun toInt(): Int
  fun toLong(): Long
  fun toUByte(): UByte
  fun toUShort(): UShort
  fun toUInt(): UInt
  fun toULong(): ULong

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

  operator fun rem(lhs: Byte): BigInt
  operator fun rem(lhs: Short): BigInt
  operator fun rem(lhs: Int): BigInt
  operator fun rem(lhs: Long): BigInt
  operator fun rem(lhs: UByte): BigInt
  operator fun rem(lhs: UShort): BigInt
  operator fun rem(lhs: UInt): BigInt
  operator fun rem(lhs: ULong): BigInt
  operator fun rem(lhs: BigInt): BigInt

  operator fun compareTo(lhs: Byte): Int
  operator fun compareTo(lhs: Short): Int
  operator fun compareTo(lhs: Int): Int
  operator fun compareTo(lhs: Long): Int
  operator fun compareTo(lhs: UByte): Int
  operator fun compareTo(lhs: UShort): Int
  operator fun compareTo(lhs: UInt): Int
  operator fun compareTo(lhs: ULong): Int
  operator fun compareTo(lhs: BigInt): Int

  operator fun unaryMinus(): BigInt

  fun toPlainString(): String

  companion object {
    val Zero: BigInt = BigIntImpl(false, byteDequeOf())
  }
}

