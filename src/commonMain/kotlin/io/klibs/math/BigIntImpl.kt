package io.klibs.math

import io.klibs.collections.ByteDeque
import io.klibs.collections.IntDeque

internal open class BigIntImpl(
  private var negative: Boolean,
  private val digits: ByteDeque,
) : BigInt {

  override val isNegative: Boolean
    get() = negative && digits.isNotEmpty()

  override val isPositive: Boolean
    get() = !negative && digits.isNotEmpty()

  override val isZero: Boolean
    get() = digits.isEmpty()

  // region Maintenance

  private fun trimToSize() {
    while (digits.isNotEmpty() && digits.peekFirst() == BYTE_0)
      digits.popFirst()

    digits.trimToSize()
  }

  // endregion Maintenance

  // region Fits

  // region Fits Signed

  override fun fitsByte(): Boolean {
    trimToSize()

    if (digits.size < 3)
      return true
    if (digits.size > 3)
      return false
    if (digits[0] > BYTE_1)
      return false
    if (digits[1] > BYTE_2)
      return false
    if (digits[1] == BYTE_2) {
      if (isNegative) {
        if (digits[2] > BYTE_8)
          return false
      } else {
        if (digits[2] > BYTE_7)
          return false
      }
    }

    return true
  }

  override fun fitsShort(): Boolean {
    trimToSize()

    return if (digits.size < 5)
      true
    else if (digits.size > 5)
      false
    else if (digits[0] > BYTE_3)
      false
    else if (digits[0] < BYTE_3)
      true
    else if (digits[1] > BYTE_2)
      false
    else if (digits[1] < BYTE_2)
      true
    else if (digits[2] > BYTE_7)
      false
    else if (digits[2] < BYTE_7)
      true
    else if (digits[3] > BYTE_6)
      false
    else if (digits[3] < BYTE_6)
      true
    else if (isNegative)
      digits[4] <= BYTE_8
    else
      digits[4] <= BYTE_7
  }

  override fun fitsInt(): Boolean {
    trimToSize()

    return if (digits.size < 10)
      true
    else if (digits.size > 10)
      false
    else if (digits[0] > BYTE_2)
      false
    else if (digits[0] < BYTE_2)
      true
    else if (digits[1] > BYTE_1)
      false
    else if (digits[1] < BYTE_1)
      true
    else if (digits[2] > BYTE_4)
      false
    else if (digits[2] < BYTE_4)
      true
    else if (digits[3] > BYTE_7)
      false
    else if (digits[3] < BYTE_7)
      true
    else if (digits[4] > BYTE_4)
      false
    else if (digits[4] < BYTE_4)
      true
    else if (digits[5] > BYTE_8)
      false
    else if (digits[5] < BYTE_8)
      true
    else if (digits[6] > BYTE_3)
      false
    else if (digits[6] < BYTE_3)
      true
    else if (digits[7] > BYTE_6)
      false
    else if (digits[7] < BYTE_6)
      true
    else if (digits[8] > BYTE_4)
      false
    else if (digits[8] < BYTE_4)
      true
    else if (isNegative)
      digits[9] <= BYTE_8
    else
      digits[9] <= BYTE_7
  }

  override fun fitsLong(): Boolean {
    trimToSize()

    return if (digits.size > 19)
      false
    else if (digits.size < 19)
      true
    else if (digits[0] < BYTE_9)
      true
    else if (digits[1] > BYTE_2)
      false
    else if (digits[1] < BYTE_2)
      true
    else if (digits[2] > BYTE_2)
      false
    else if (digits[2] < BYTE_2)
      true
    else if (digits[3] > BYTE_3)
      false
    else if (digits[3] < BYTE_3)
      true
    else if (digits[4] > BYTE_3)
      false
    else if (digits[4] < BYTE_3)
      true
    else if (digits[5] > BYTE_7)
      false
    else if (digits[5] < BYTE_7)
      true
    else if (digits[6] > BYTE_2)
      false
    else if (digits[6] < BYTE_2)
      true
    else if (digits[7] > BYTE_0)
      false
    else if (digits[8] > BYTE_3)
      false
    else if (digits[8] < BYTE_3)
      true
    else if (digits[9] > BYTE_6)
      false
    else if (digits[9] < BYTE_6)
      true
    else if (digits[10] > BYTE_8)
      false
    else if (digits[10] < BYTE_8)
      true
    else if (digits[11] > BYTE_5)
      false
    else if (digits[11] < BYTE_5)
      true
    else if (digits[12] > BYTE_4)
      false
    else if (digits[12] < BYTE_4)
      true
    else if (digits[13] > BYTE_7)
      false
    else if (digits[13] < BYTE_7)
      true
    else if (digits[14] > BYTE_7)
      false
    else if (digits[14] < BYTE_7)
      true
    else if (digits[15] > BYTE_5)
      false
    else if (digits[15] < BYTE_5)
      true
    else if (digits[16] > BYTE_8)
      false
    else if (digits[16] < BYTE_8)
      true
    else if (digits[17] > BYTE_0)
      false
    else if (isNegative)
      digits[18] <= BYTE_8
    else
      digits[18] <= BYTE_7
  }

  // endregion Fits Signed

  // region Fits Unsigned

  override fun fitsUByte(): Boolean {
    if (isNegative)
      return false

    trimToSize()

    return if (digits.size > 3)
      false
    else if (digits.size < 3)
      true
    else if (digits[0] > BYTE_2)
      false
    else if (digits[0] < BYTE_2)
      true
    else if (digits[1] > BYTE_5)
      false
    else if (digits[1] < BYTE_5)
      true
    else
      digits[2] <= BYTE_5
  }

  override fun fitsUShort(): Boolean {
    if (isNegative)
      return false

    trimToSize()

    return if (digits.size > 5)
      false
    else if (digits.size < 5)
      true
    else if (digits[0] > BYTE_6)
      false
    else if (digits[0] > BYTE_6)
      true
    else if (digits[1] > BYTE_5)
      false
    else if (digits[1] > BYTE_5)
      true
    else if (digits[2] > BYTE_5)
      false
    else if (digits[2] > BYTE_5)
      true
    else if (digits[3] > BYTE_3)
      false
    else if (digits[3] > BYTE_3)
      true
    else
      digits[4] <= BYTE_5
  }

  override fun fitsUInt(): Boolean {
    if (isNegative)
      return false

    trimToSize()

    return if (digits.size > 10)
      false
    else if (digits.size < 10)
      true
    else if (digits[0] > BYTE_4)
      false
    else if (digits[0] < BYTE_4)
      true
    else if (digits[1] > BYTE_2)
      false
    else if (digits[1] < BYTE_2)
      true
    else if (digits[2] < BYTE_9)
      true
    else if (digits[3] > BYTE_4)
      false
    else if (digits[3] < BYTE_4)
      true
    else if (digits[4] < BYTE_9)
      true
    else if (digits[5] > BYTE_6)
      false
    else if (digits[5] < BYTE_6)
      true
    else if (digits[6] > BYTE_7)
      false
    else if (digits[6] < BYTE_7)
      true
    else if (digits[7] > BYTE_2)
      false
    else if (digits[7] < BYTE_2)
      true
    else if (digits[8] < BYTE_9)
      true
    else
      digits[9] <= BYTE_5
  }

  override fun fitsULong(): Boolean {
    if (isNegative)
      return false

    trimToSize()

    return if (digits.size > 20)
      false
    else if (digits.size < 20)
      true
    else if (digits[0] > BYTE_1)
      false
    else if (digits[0] < BYTE_1)
      true
    else if (digits[1] > BYTE_8)
      false
    else if (digits[1] < BYTE_8)
      true
    else if (digits[2] > BYTE_4)
      false
    else if (digits[2] < BYTE_4)
      true
    else if (digits[3] > BYTE_4)
      false
    else if (digits[3] < BYTE_4)
      true
    else if (digits[4] > BYTE_6)
      false
    else if (digits[4] < BYTE_6)
      true
    else if (digits[5] > BYTE_7)
      false
    else if (digits[5] < BYTE_7)
      true
    else if (digits[6] > BYTE_4)
      false
    else if (digits[6] < BYTE_4)
      true
    else if (digits[7] > BYTE_4)
      false
    else if (digits[7] < BYTE_4)
      true
    else if (digits[8] > BYTE_0)
      false
    else if (digits[9] > BYTE_7)
      false
    else if (digits[9] < BYTE_7)
      true
    else if (digits[10] > BYTE_3)
      false
    else if (digits[10] < BYTE_3)
      true
    else if (digits[11] > BYTE_7)
      false
    else if (digits[11] < BYTE_7)
      true
    else if (digits[12] > BYTE_0)
      false
    else if (digits[13] < BYTE_9)
      true
    else if (digits[14] > BYTE_5)
      false
    else if (digits[14] < BYTE_5)
      true
    else if (digits[15] > BYTE_5)
      false
    else if (digits[15] < BYTE_5)
      true
    else if (digits[16] > BYTE_1)
      false
    else if (digits[16] < BYTE_1)
      true
    else if (digits[17] > BYTE_6)
      false
    else if (digits[17] < BYTE_6)
      true
    else if (digits[18] > BYTE_1)
      false
    else if (digits[18] < BYTE_1)
      true
    else
      digits[19] <= BYTE_5
  }

  // endregion Fits Unsigned

  // endregion Fits

  // region To

  override fun toByte(): Byte {
    if (!fitsByte())
      throw NumberCastException()

    var out: Byte = 0
    digits.peekEach { out = (out * 10 + it.toByte()).toByte() }
    return if (isNegative) (-out).toByte() else out
  }

  override fun toShort(): Short {
    if (!fitsShort())
      throw NumberCastException()

    var out: Short = 0
    digits.peekEach { out = (out * 10 + it.toShort()).toShort() }
    return if (isNegative) (-out).toShort() else out
  }

  override fun toInt(): Int {
    if (!fitsInt())
      throw NumberCastException()

    var out = 0
    digits.peekEach { out = out * 10 + it.toInt() }
    return if (isNegative) -out else out
  }

  override fun toLong(): Long {
    if (!fitsLong())
      throw NumberCastException()

    var out = 0L
    digits.peekEach { out = out * 10 + it.toLong() }
    return if (isNegative) -out else out
  }

  override fun toUByte(): UByte {
    if (!fitsUByte())
      throw NumberCastException()

    var out: UByte = 0u
    digits.peekEach { out = (out * 10u + it.toUByte()).toUByte() }
    return out
  }

  override fun toUShort(): UShort {
    if (!fitsUShort())
      throw NumberCastException()

    var out: UShort = 0u
    digits.peekEach { out = (out * 10u + it.toUShort()).toUShort() }
    return out
  }

  override fun toUInt(): UInt {
    if (!fitsUInt())
      throw NumberCastException()

    var out = 0u
    digits.peekEach { out = out * 10u + it.toUInt() }
    return out
  }

  override fun toULong(): ULong {
    if (!fitsUInt())
      throw NumberCastException()

    var out = 0uL
    digits.peekEach { out = out * 10u + it.toULong() }
    return out
  }

  // endregion To

  // region Plus

  override fun plus(rhs: Byte): BigInt = plus(rhs.toLong())
  override fun plus(rhs: Short): BigInt = plus(rhs.toLong())
  override fun plus(rhs: Int): BigInt = plus(rhs.toLong())

  override fun plus(rhs: Long): BigInt {
    return if (rhs == 0L)
      BigIntImpl(isNegative, digits)
    else if (isNegative) {
      if (rhs > 0L)
        posMinus(rhs)
      else
        posPlus(-rhs)
    } else {
      if (rhs > 0L)
        posPlus(rhs)
      else
        posMinus(-rhs)
    }
  }

  private fun posPlus(rhs: Long): BigInt {
    val out = ByteDeque(digits.size)
    var rem = rhs

    var i = digits.lastIndex
    while (i >= 0) {
      var sum = digits[i--] + (rem % 10).toByte()
      rem /= 10

      if (sum >= 10) {
        rem++
        sum -= 10
      }

      out.pushFirst(sum.toByte())
    }

    out.ensureCapacity(out.size + rem.decStringWidth())

    while (rem > 0) {
      val digitToAdd = (rem % 10).toByte()
      out.pushFirst(digitToAdd)
      rem /= 10
    }

    out.trimToSize()

    return BigIntImpl(isNegative, out)
  }

  override fun plus(rhs: UByte) = plus(rhs.toULong())
  override fun plus(rhs: UShort) = plus(rhs.toULong())
  override fun plus(rhs: UInt) = plus(rhs.toULong())

  override fun plus(rhs: ULong): BigInt {
    return if (rhs == 0uL)
      BigIntImpl(isNegative, digits)
    else if (isNegative)
      posMinus(rhs)
    else
      posPlus(rhs)
  }

  private fun posPlus(rhs: ULong): BigInt {
    val out = ByteDeque(digits.size)
    var rem = rhs

    var i = digits.lastIndex
    while (i >= 0) {
      var sum = digits[i--] + (rem % 10u).toByte()
      rem /= 10u

      if (sum >= 10) {
        rem++
        sum -= 10
      }

      out.pushFirst(sum.toByte())
    }

    out.ensureCapacity(out.size + rem.decStringWidth())

    while (rem > 0u) {
      val digitToAdd = (rem % 10u).toByte()
      out.pushFirst(digitToAdd)
      rem /= 10u
    }

    out.trimToSize()

    return BigIntImpl(isNegative, out)
  }

  override fun plus(rhs: BigInt): BigInt {
    return if (isNegative && rhs.isNegative)
      posPlus(rhs as BigIntImpl)
    else
      posMinus(rhs as BigIntImpl)
  }

  private fun posPlus(rhs: BigIntImpl): BigInt {
    val out = ByteDeque(max(rhs.digits.size, digits.size))

    var sum: Int
    var car = 0

    for (i in out.capacity downTo 1) {
      sum = (digits.getOrZero(i) + rhs.digits.getOrZero(i)) + car
      car = sum / 10
      out.pushFirst((sum % 10).toByte())
    }

    if (car > 0)
      out.pushFirst(car.toByte())

    out.trimToSize()

    return BigIntImpl(isNegative, out)
  }

  // endregion Plus

  // region Minus

  override fun minus(rhs: Byte) = minus(rhs.toLong())
  override fun minus(rhs: Short) = minus(rhs.toLong())
  override fun minus(rhs: Int) = minus(rhs.toLong())

  override fun minus(rhs: Long): BigInt {
    return if (rhs == 0L)
      BigIntImpl(isNegative, digits)
    else if (isNegative) {
      if (rhs < 0L)
        posMinus(-rhs)
      else
        posPlus(rhs)
    } else {
      if (rhs < 0L)
        posPlus(-rhs)
      else
        posMinus(rhs)
    }
  }

  private fun posMinus(rhs: Long): BigInt {
    return posMinus(rhs.toBigInt() as BigIntImpl)
  }

  override fun minus(rhs: UByte) = minus(rhs.toULong())
  override fun minus(rhs: UShort) = minus(rhs.toULong())
  override fun minus(rhs: UInt) = minus(rhs.toULong())

  override fun minus(rhs: ULong): BigInt {
    return if (rhs == 0uL)
      BigIntImpl(isNegative, digits)
    else if (isNegative)
      posPlus(rhs)
    else
      posMinus(rhs)
  }

  private fun posMinus(rhs: ULong): BigInt {
    return posMinus(rhs.toBigInt() as BigIntImpl)
  }

  override fun minus(rhs: BigInt): BigInt {
    TODO()
  }

  private fun posMinus(rhs: BigIntImpl): BigInt {
    if (this < rhs)
      return -rhs.posMinus(this)

    val out = ByteDeque(digits.size)
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

      out.pushFirst((r - l).toByte())
      i--
      j--
    }

    while (out.peekFirst() == BYTE_0)
      out.popFirst()

    out.trimToSize()

    return BigIntImpl(isNegative, out)
  }

  // endregion Minus

  // region Times

  override fun times(rhs: Byte) = times(rhs.toBigInt())
  override fun times(rhs: Short) = times(rhs.toBigInt())
  override fun times(rhs: Int) = times(rhs.toBigInt())
  override fun times(rhs: Long) = times(rhs.toBigInt())
  override fun times(rhs: UByte) = times(rhs.toBigInt())
  override fun times(rhs: UShort) = times(rhs.toBigInt())
  override fun times(rhs: UInt) = times(rhs.toBigInt())
  override fun times(rhs: ULong) = times(rhs.toBigInt())
  override fun times(rhs: BigInt): BigInt {
    if (isZero || rhs.isZero)
      return BigInt.Zero

    rhs as BigIntImpl

    return BigIntImpl(negative != rhs.negative, multiply(this.digits, rhs.digits))
  }

  // endregion Times

  // region Div

  override fun div(rhs: Byte) = div(rhs.toBigInt())
  override fun div(rhs: Short) = div(rhs.toBigInt())
  override fun div(rhs: Int) = div(rhs.toBigInt())
  override fun div(rhs: Long) = div(rhs.toBigInt())
  override fun div(rhs: UByte) = div(rhs.toBigInt())
  override fun div(rhs: UShort) = div(rhs.toBigInt())
  override fun div(rhs: UInt) = div(rhs.toBigInt())
  override fun div(rhs: ULong) = div(rhs.toBigInt())

  override fun div(rhs: BigInt): BigInt {
    if (isZero || this < rhs)
      return BigInt.Zero
    if (rhs.isZero)
      TODO("division by zero exception")
    if (this == rhs)
      return BigInt.One
    if (rhs == BigInt.One)
      return this
    if (rhs == BigInt.NegativeOne)
      return -this

    rhs as BigIntImpl

    return BigIntImpl(negative != rhs.isNegative, divide(this.digits, rhs.digits))
  }


  // endregion Div

  // region Rem

  override fun rem(rhs: Byte) = rem(rhs.toBigInt())
  override fun rem(rhs: Short) = rem(rhs.toBigInt())
  override fun rem(rhs: Int) = rem(rhs.toBigInt())
  override fun rem(rhs: Long) = rem(rhs.toBigInt())
  override fun rem(rhs: UByte) = rem(rhs.toBigInt())
  override fun rem(rhs: UShort) = rem(rhs.toBigInt())
  override fun rem(rhs: UInt) = rem(rhs.toBigInt())
  override fun rem(rhs: ULong) = rem(rhs.toBigInt())

  override fun rem(rhs: BigInt): BigInt {
    TODO("Not yet implemented")
  }

  // endregion Rem

  override fun compareTo(rhs: Byte) = compareTo(rhs.toBigInt())
  override fun compareTo(rhs: Short) = compareTo(rhs.toBigInt())
  override fun compareTo(rhs: Int) = compareTo(rhs.toBigInt())
  override fun compareTo(rhs: Long) = compareTo(rhs.toBigInt())
  override fun compareTo(rhs: UByte) = compareTo(rhs.toBigInt())
  override fun compareTo(rhs: UShort) = compareTo(rhs.toBigInt())
  override fun compareTo(rhs: UInt) = compareTo(rhs.toBigInt())
  override fun compareTo(rhs: ULong) = compareTo(rhs.toBigInt())

  override fun compareTo(rhs: BigInt): Int {
    rhs as BigIntImpl

    if (digits.size > rhs.digits.size)
      return 1
    else if (digits.size < rhs.digits.size)
      return -1

    var i = 0
    while (i < digits.size) {
      if (digits[i] > rhs.digits[i])
        return 1
      else if (digits[i] < rhs.digits[i])
        return -1
      else
        i++
    }

    return 0
  }

  override fun unaryMinus(): BigInt = BigIntImpl(!isNegative, digits)

  override fun toPlainString(): String {
    if (digits.isEmpty())
      return "0"

    val out = if (isNegative)
      StringBuilder(digits.size + 1).append('-')
    else
      StringBuilder(digits.size)

    var i = 0;
    while (i < digits.size) {
      out.append('0' + digits[i++].toInt())
    }

    return out.toString()
  }

  override fun equals(other: Any?): Boolean {
    if (other === this)
      return true

    if (other is BigIntImpl)
      return negative == other.negative && digits.contentEquals(other.digits)

    return when (other) {
      is Int    -> other.toBigInt() == this
      is Long   -> other.toBigInt() == this
      is UInt   -> other.toBigInt() == this
      is ULong  -> other.toBigInt() == this
      is Byte   -> other.toBigInt() == this
      is UByte  -> other.toBigInt() == this
      is Short  -> other.toBigInt() == this
      is UShort -> other.toBigInt() == this
      else      -> false
    }
  }

  override fun hashCode(): Int {
    return negative.hashCode() + digits.hashCode()
  }

  override fun toString() = toPlainString()
}

private fun multiply(a: ByteDeque, b: ByteDeque): ByteDeque {
  val t = IntDeque(a.size + b.size)
  var c = 0
  var s: Int
  var p: Int

  for (i in 0 .. a.lastIndex) {
    for (j in 0 .. b.lastIndex) {
      p = i + j

      if (p < t.size) {
        t[p] = (t[p] + a[i] * b[j])
      } else {
        while (p > t.size) {
          t.pushLast(0)
        }

        t.pushLast(a[i] * b[j])
      }
    }
  }

  for (i in t.lastIndex downTo 0) {
    s = c + t[i]
    t[i] = (s % 10)
    c = s / 10
  }

  while (c > 0) {
    t.pushFirst((c % 10))
    c /= 10
  }

  trimToSize(t)

  val o = ByteDeque(t.size)
  for (v in t) {
    o.pushLast(v.toByte())
  }

  return o
}

private fun divide(dividend: ByteDeque, divisor: ByteDeque): ByteDeque {
  val quotient = ByteDeque(dividend.size)
  val remainder = ByteDeque()

  for




}

private fun trimToSize(a: IntDeque) {
  while (a.size > 0 && a.peekFirst() == 0)
    a.popFirst()

  a.trimToSize()
}