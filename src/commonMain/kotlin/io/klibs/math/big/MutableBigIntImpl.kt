package io.klibs.math.big

import io.klibs.collections.LongDeque
import io.klibs.collections.UIntDeque
import io.klibs.collections.uintDequeOf

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

  // region Plus

  override fun plusAssign(rhs: BigInt) {
    val r = unpack(rhs)

    if (this.isZero)
      this.chunks = r.chunks.copyOf()
    else if (r.isZero)
      return
    else if (isNegative != r.isNegative)
      posMinusAssign(r)
    else
      posPlusAssign(r)
  }

  private fun posPlusAssign(rhs: MutableBigIntImpl) {
    val x  = chunks.size
    val y  = rhs.chunks.size
    var rl = max(x, y)

    chunks.ensureCapacity(rl)

    var s: Long
    var c = 0L

    while (rl > 0) {
      s = c + (if (rl < x) chunks[rl].toLong() and L_M else 0) + (if (rl < y) rhs.chunks[rl].toLong() and L_M else 0)
      if (rl < x)
        chunks[rl] = (s and L_M).toUInt()
      else
        chunks.pushFirst((s and L_M).toUInt())
      c = s ushr 32
      rl--
    }

    while (c > 0) {
      chunks.pushFirst((c and L_M).toUInt())
      c = c ushr 32
    }

    trimToSize(chunks)
  }

  override fun plus(rhs: BigInt): MutableBigInt {
    val out = MutableBigIntImpl(sign, chunks)
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
      var r: Long
      var l: Long

      var i = chunks.lastIndex
      var j = rhs.chunks.lastIndex
      while (i >= 0) {
        r = chunks[i].toLong()
        l = if (j > -1) rhs.chunks[j].toLong() else 0L

        if (borrow) {
          r--
          borrow = false
        }

        if (l > r) {
          borrow = true
          r += 10000000000
        }

        chunks[i] = (r - l).toUInt()
        i--
        j--
      }

      trimToSize(chunks)
    }
  }

  override fun minus(rhs: BigInt): MutableBigInt {
    val out = MutableBigIntImpl(sign, chunks.copyOf())
    out -= rhs
    return out
  }

  // endregion Minus

  // region Times

  override fun timesAssign(rhs: BigInt) {
    if (isZero)
      return

    if (rhs.isZero) {
      chunks.clear()
      sign = 0
      return
    }

    val neg = isNegative != rhs.isNegative
    internalTimesAssign(unpack(rhs))
    sign = if (neg) -1 else 1
  }

  private fun internalTimesAssign(rhs: MutableBigIntImpl) {
    var n = chunks.size
    val m = rhs.chunks.size
    val v = LongDeque(n + m)

    for (i in 1 .. v.size)
      v.pushLast(0)

    for (i in 0 until n)
      for (j in 0 until m)
        v[i + j] += (chunks[i].toLong() and L_M) * (rhs.chunks[j].toLong() and L_M)

    n += m
    chunks.ensureCapacity(v.size)

    var s: Long
    var t = 0L
    for (i in 0 until n) {
      s = t + v[i]
      v[i] = (s and L_M)
      t = (s ushr 32)
      chunks[i] = (v[i] and L_M).toUInt()
    }

    var i = n - 1
    while (i >= 1 && v[i--] == 0L)
      chunks.popLast()
  }

  override fun times(rhs: BigInt): MutableBigInt {
    val out = MutableBigIntImpl(sign, chunks.copyOf())
    out *= rhs
    return out
  }

  // endregion Times

  override fun compareTo(rhs: BigInt): Int {
    val r = unpack(rhs)

    // If this instance has more digits than the other instance, then this one
    // is the larger value.
    if (chunks.size > r.chunks.size)
      return 1

    // If the other instance has more digits than this one, then the other one
    // is the larger value.
    if (chunks.size < r.chunks.size)
      return -1

    var i = 0
    while (i < chunks.size) {
      if (chunks[i] > r.chunks[i])
        return 1

      if (chunks[i] < r.chunks[i])
        return -1

      i++
    }

    return 0
  }

  @OptIn(ExperimentalUnsignedTypes::class)
  override fun unaryMinus(): MutableBigInt {
    return if (this.isZero) {
      MutableBigIntImpl(0, uintDequeOf())
    } else {
      MutableBigIntImpl(sign, chunks.copyOf()).apply { flipNegative() }
    }
  }

  override fun toPlainString(): String {
    if (chunks.isEmpty())
      return "0"

    val out = if (isNegative)
      StringBuilder(chunks.size + 1).append('-')
    else
      StringBuilder(chunks.size)

    for (d in chunks)
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

}