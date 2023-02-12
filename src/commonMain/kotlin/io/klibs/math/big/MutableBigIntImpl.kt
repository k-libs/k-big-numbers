package io.klibs.math.big

import io.klibs.collections.UIntDeque

internal class MutableBigIntImpl : MutableBigInt {
  private var sign: Byte
  private var chunks: UIntDeque

  override val isZero: Boolean
    get() = sign == B_0

  override val isNegative: Boolean
    get() = sign < B_0

  override val isPositive: Boolean
    get() = sign > B_0

  constructor(sign: Byte, digits: UIntDeque) {
    this.sign = sign
    this.chunks = digits
  }

  override fun abs(): MutableBigInt =
    if (isZero)
      MutableBigInt.zero()
    else if (isNegative)
      -this
    else
      this.clone()

  override fun unaryMinus(): MutableBigInt = clone().apply { flipNegative() }

  override fun clone(): MutableBigInt = if (isZero) MutableBigInt.zero() else MutableBigIntImpl(sign, chunks.copyOf())

  override fun toString(radix: BigIntRadix): String {
    if (isZero)
      return "0"

    val abs = internalAbs()
    val sb = if (isNegative) StringBuilder(chunks.size * 10 + 1).append('-') else StringBuilder(chunks.size * 10)

    toString(abs, sb, radix, 0)

    return sb.toString()
  }

  private fun internalAbs() = if (isNegative) -this else this

  private fun toString(u: BigInt, sb: StringBuilder, r: BigIntRadix, digits: Int) {

  }

  private inline fun flipNegative() {
    sign = if (sign > 0) -1 else if (sign < 0) 1 else 0
  }

  private inline fun unpack(rhs: BigInt) =
    when (rhs) {
      is ImmutableBigInt   -> rhs.raw as MutableBigIntImpl
      is MutableBigIntImpl -> rhs
    }
}