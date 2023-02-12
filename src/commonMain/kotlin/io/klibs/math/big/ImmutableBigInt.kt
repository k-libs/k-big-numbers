package io.klibs.math.big

internal data class ImmutableBigInt(val raw: MutableBigInt) : BigInt by raw {

//  override fun plus(rhs: BigInt): BigInt = ImmutableBigInt(raw.plus(rhs))

//  override fun minus(rhs: BigInt): BigInt = ImmutableBigInt(raw.minus(rhs))

//  override fun times(rhs: BigInt): BigInt = ImmutableBigInt(raw.times(rhs))

  override fun abs(): BigInt =
    if (raw.isZero)
      BigInt.Zero
    else if (raw.isNegative)
      ImmutableBigInt(-raw)
    else
      this

  override fun unaryMinus(): BigInt = ImmutableBigInt(raw.unaryMinus())
}