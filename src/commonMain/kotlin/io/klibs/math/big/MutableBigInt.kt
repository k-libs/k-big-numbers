package io.klibs.math.big

import io.klibs.collections.byteDequeOf

sealed interface MutableBigInt : BigInt {

  override fun plus(rhs: BigInt): MutableBigInt

  operator fun plusAssign(rhs: BigInt)

  override fun minus(rhs: BigInt): MutableBigInt

  operator fun minusAssign(rhs: BigInt)

  override fun times(rhs: BigInt): MutableBigInt

  operator fun timesAssign(rhs: BigInt)

  override fun unaryMinus(): MutableBigInt

  companion object {
    fun zero(): MutableBigInt = MutableBigIntImpl(0, byteDequeOf())

    fun one(): MutableBigInt = MutableBigIntImpl(0, byteDequeOf(1))

    inline fun ofByte(b: Byte) = mutableBigIntOf(b)
    inline fun ofShort(s: Short) = mutableBigIntOf(s)
    inline fun ofInt(i: Int) = mutableBigIntOf(i)
    inline fun ofLong(l: Long) = mutableBigIntOf(l)

    inline fun ofUByte(b: UByte) = mutableBigIntOf(b)
    inline fun ofUShort(s: UShort) = mutableBigIntOf(s)
    inline fun ofUInt(i: UInt) = mutableBigIntOf(i)
    inline fun ofULong(l: ULong) = mutableBigIntOf(l)
  }
}