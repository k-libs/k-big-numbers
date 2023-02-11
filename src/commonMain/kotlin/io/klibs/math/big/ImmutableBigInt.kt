package io.klibs.math.big

internal data class ImmutableBigInt(val raw: MutableBigInt) : BigInt by raw {

  override fun plus(rhs: BigInt): BigInt = ImmutableBigInt(raw.plus(rhs))

  override fun minus(rhs: BigInt): BigInt = ImmutableBigInt(raw.minus(rhs))

  override fun times(rhs: BigInt): BigInt = ImmutableBigInt(raw.times(rhs))

  override fun unaryMinus(): BigInt = ImmutableBigInt(raw.unaryMinus())
}