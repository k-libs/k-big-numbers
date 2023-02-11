package io.klibs.math.big

import io.klibs.collections.ByteDeque
import io.klibs.collections.IntDeque
import io.klibs.collections.byteDequeOf

internal class MutableBigIntImpl : MutableBigInt {
  private var sign: Byte
  private var digits: ByteDeque

  override val isZero: Boolean
    get() = sign == B_0

  override val isNegative: Boolean
    get() = sign < B_0

  override val isPositive: Boolean
    get() = sign > B_0

  constructor(sign: Byte, digits: ByteDeque) {
    this.sign = sign
    this.digits = digits
  }

  // region Plus

  override fun plusAssign(rhs: BigInt) {
    val r = unpack(rhs)

    if (this.isZero)
      this.digits = r.digits.clone()
    else if (r.isZero)
      return
    else if (isNegative != r.isNegative)
      posMinusAssign(r)
    else
      posPlusAssign(r)
  }

  private fun posPlusAssign(rhs: MutableBigIntImpl) {
    digits.ensureCapacity(max(digits.size, rhs.digits.size))

    var carry = 0

    for (i in digits.lastIndex downTo 0) {
      val sum = carry + digits[i] + if (i < rhs.digits.size) rhs.digits[i] else 0
      digits[i] = (sum % 10).toByte()
      carry = sum / 10
    }

    while (carry > 0) {
      digits.pushFirst((carry % 10).toByte())
      carry /= 10
    }
  }

  override fun plus(rhs: BigInt): MutableBigInt {
    val out = MutableBigIntImpl(sign, digits)
    out += rhs
    return out
  }

  // endregion Plus

  // region Minus

  override fun minusAssign(rhs: BigInt) {
    if (rhs.isZero)
      return

    if (isZero) {
      posPlusAssign(unpack(rhs))
      sign = if (rhs.isNegative) -1 else 1
    } else if (isPositive == rhs.isPositive) {
      posMinusAssign(unpack(rhs))
    } else {
      posPlusAssign(unpack(rhs))
    }
  }

  private fun posMinusAssign(rhs: MutableBigIntImpl) {
    if (this < rhs) {
      rhs.posMinusAssign(this)
      flipNegative()
    } else {
      var borrow = false
      var r: Int
      var l: Int

      var i = digits.lastIndex
      var j = rhs.digits.lastIndex
      while (i >= 0) {
        r = digits[i].toInt()
        l = if (j > -1) rhs.digits[j].toInt() else 0

        if (borrow) {
          r--
          borrow = false
        }

        if (l > r) {
          borrow = true
          r += 10
        }

        digits[i] = (r - l).toByte()
        i--
        j--
      }

      trimToSize(digits)
    }
  }

  override fun minus(rhs: BigInt): MutableBigInt {
    val out = MutableBigIntImpl(sign, digits.clone())
    out -= rhs
    return out
  }

  // endregion Minus

  // region Times

  override fun timesAssign(rhs: BigInt) {
    if (isZero)
      return

    if (rhs.isZero) {
      digits.clear()
      sign = 0
      return
    }

    val neg = isNegative != rhs.isNegative
    internalTimesAssign(unpack(rhs))
    sign = if (neg) -1 else 1
  }

  private fun internalTimesAssign(rhs: MutableBigIntImpl) {
    var n = digits.size
    val m = rhs.digits.size
    val v = IntDeque(n + m)

    for (i in 1 .. v.size)
      v.pushLast(0)

    for (i in 0 until n)
      for (j in 0 until m)
        v[i + j] += digits[i] * rhs.digits[j]

    n += m
    digits.ensureCapacity(v.size)

    var s: Int
    var t = 0
    for (i in 0 until n) {
      s = t + v[i]
      v[i] = s % 10
      t = s / 10
      digits[i] = v[i].toByte()
    }

    var i = n - 1
    while (i >= 1 && v[i--] == 0)
      digits.popLast()
  }

  override fun times(rhs: BigInt): MutableBigInt {
    val out = MutableBigIntImpl(sign, digits.clone())
    out *= rhs
    return out
  }

  // endregion Times

  override fun compareTo(rhs: BigInt): Int {
    val r = unpack(rhs)

    // If this instance has more digits than the other instance, then this one
    // is the larger value.
    if (digits.size > r.digits.size)
      return 1

    // If the other instance has more digits than this one, then the other one
    // is the larger value.
    if (digits.size < r.digits.size)
      return -1

    var i = 0
    while (i < digits.size) {
      if (digits[i] > r.digits[i])
        return 1

      if (digits[i] < r.digits[i])
        return -1

      i++
    }

    return 0
  }

  override fun unaryMinus(): MutableBigInt {
    return if (this.isZero) {
      MutableBigIntImpl(0, byteDequeOf())
    } else {
      MutableBigIntImpl(sign, digits.clone()).apply { flipNegative() }
    }
  }

  override fun toPlainString(): String {
    if (digits.isEmpty())
      return "0"

    val out = if (isNegative)
      StringBuilder(digits.size + 1).append('-')
    else
      StringBuilder(digits.size)

    for (d in digits)
      out.append(d)

    return out.toString()
  }

  override fun toString() = toPlainString()

  private fun flipNegative() {
    if (sign > 0)
      sign = -1
    else if (sign < 0)
      sign = 1
  }

  private inline fun unpack(rhs: BigInt) =
    when (rhs) {
      is ImmutableBigInt   -> rhs.raw as MutableBigIntImpl
      is MutableBigIntImpl -> rhs
    }

  private inline fun trimToSize(b: ByteDeque) {
    while (b.size > 0 && b.peekFirst() == B_0)
      b.popFirst()
    b.trimToSize()
  }
}