package io.klibs.math

import io.klibs.collections.ByteDeque

internal class BigIntImpl(
  private val negative: Boolean,
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

  override fun plus(lhs: Byte): BigInt = plus(lhs.toLong())
  override fun plus(lhs: Short): BigInt = plus(lhs.toLong())
  override fun plus(lhs: Int): BigInt = plus(lhs.toLong())

  override fun plus(lhs: Long): BigInt {
    return if (lhs == 0L)
      BigIntImpl(isNegative, digits)
    else if (isNegative) {
      if (lhs > 0L)
        posMinus(lhs)
      else
        posPlus(-lhs)
    } else {
      if (lhs > 0L)
        posPlus(lhs)
      else
        posMinus(-lhs)
    }
  }

  private fun posPlus(lhs: Long): BigInt {
    val out = ByteDeque(digits.size)
    var rem = lhs

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

  override fun plus(lhs: UByte) = plus(lhs.toULong())
  override fun plus(lhs: UShort) = plus(lhs.toULong())
  override fun plus(lhs: UInt) = plus(lhs.toULong())

  override fun plus(lhs: ULong): BigInt {
    return if (lhs == 0uL)
      BigIntImpl(isNegative, digits)
    else if (isNegative)
      posMinus(lhs)
    else
      posPlus(lhs)
  }

  private fun posPlus(lhs: ULong): BigInt {
    val out = ByteDeque(digits.size)
    var rem = lhs

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

  override fun plus(lhs: BigInt): BigInt {
    return if (isNegative && lhs.isNegative)
      posPlus(lhs as BigIntImpl)
    else
      posMinus(lhs as BigIntImpl)
  }

  private fun posPlus(lhs: BigIntImpl): BigInt {
    val out = ByteDeque(max(lhs.digits.size, digits.size))

    var sum: Int
    var car = 0

    for (i in out.capacity downTo 1) {
      sum = (digits.getOrZero(i) + lhs.digits.getOrZero(i)) + car
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

  override fun minus(lhs: Byte) = minus(lhs.toLong())
  override fun minus(lhs: Short) = minus(lhs.toLong())
  override fun minus(lhs: Int) = minus(lhs.toLong())

  override fun minus(lhs: Long): BigInt {
    return if (lhs == 0L)
      BigIntImpl(isNegative, digits)
    else if (isNegative) {
      if (lhs < 0L)
        posMinus(-lhs)
      else
        posPlus(lhs)
    } else {
      if (lhs < 0L)
        posPlus(-lhs)
      else
        posMinus(lhs)
    }
  }

  private fun posMinus(lhs: Long): BigInt {
    return posMinus(lhs.toBigInt() as BigIntImpl)
  }

  override fun minus(lhs: UByte) = minus(lhs.toULong())
  override fun minus(lhs: UShort) = minus(lhs.toULong())
  override fun minus(lhs: UInt) = minus(lhs.toULong())

  override fun minus(lhs: ULong): BigInt {
    return if (lhs == 0uL)
      BigIntImpl(isNegative, digits)
    else if (isNegative)
      posPlus(lhs)
    else
      posMinus(lhs)
  }

  private fun posMinus(lhs: ULong): BigInt {
    return posMinus(lhs.toBigInt() as BigIntImpl)
  }

  override fun minus(lhs: BigInt): BigInt {
    TODO()
  }

  private fun posMinus(lhs: BigIntImpl): BigInt {
    if (this < lhs)
      return -lhs.posMinus(this)

    val out = ByteDeque(digits.size)
    var borrow = false
    var r: Int
    var l: Int

    var i = digits.lastIndex
    var j = lhs.digits.lastIndex
    while (i >= 0) {
      r = digits[i].toInt()
      l = if (j > -1) lhs.digits[j].toInt() else 0

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

  override fun times(lhs: Byte) = times(lhs.toBigInt())
  override fun times(lhs: Short) = times(lhs.toBigInt())
  override fun times(lhs: Int) = times(lhs.toBigInt())
  override fun times(lhs: Long) = times(lhs.toBigInt())
  override fun times(lhs: UByte) = times(lhs.toBigInt())
  override fun times(lhs: UShort) = times(lhs.toBigInt())
  override fun times(lhs: UInt) = times(lhs.toBigInt())
  override fun times(lhs: ULong) = times(lhs.toBigInt())
  override fun times(lhs: BigInt): BigInt {
    if (isZero || lhs.isZero)
      return BigInt.Zero

    TODO("Not yet implemented")
  }

  // endregion Times

  // region Div

  override fun div(lhs: Byte) = div(lhs.toBigInt())
  override fun div(lhs: Short) = div(lhs.toBigInt())
  override fun div(lhs: Int) = div(lhs.toBigInt())
  override fun div(lhs: Long) = div(lhs.toBigInt())
  override fun div(lhs: UByte) = div(lhs.toBigInt())
  override fun div(lhs: UShort) = div(lhs.toBigInt())
  override fun div(lhs: UInt) = div(lhs.toBigInt())
  override fun div(lhs: ULong) = div(lhs.toBigInt())

  override fun div(lhs: BigInt): BigInt {
    TODO("Not yet implemented")
  }

  // endregion Div

  override fun rem(lhs: Byte): BigInt {
    TODO("Not yet implemented")
  }

  override fun rem(lhs: Short): BigInt {
    TODO("Not yet implemented")
  }

  override fun rem(lhs: Int): BigInt {
    TODO("Not yet implemented")
  }

  override fun rem(lhs: Long): BigInt {
    TODO("Not yet implemented")
  }

  override fun rem(lhs: UByte): BigInt {
    TODO("Not yet implemented")
  }

  override fun rem(lhs: UShort): BigInt {
    TODO("Not yet implemented")
  }

  override fun rem(lhs: UInt): BigInt {
    TODO("Not yet implemented")
  }

  override fun rem(lhs: ULong): BigInt {
    TODO("Not yet implemented")
  }

  override fun rem(lhs: BigInt): BigInt {
    TODO("Not yet implemented")
  }

  override fun compareTo(lhs: Byte) = compareTo(lhs.toBigInt())
  override fun compareTo(lhs: Short) = compareTo(lhs.toBigInt())
  override fun compareTo(lhs: Int) = compareTo(lhs.toBigInt())
  override fun compareTo(lhs: Long) = compareTo(lhs.toBigInt())
  override fun compareTo(lhs: UByte) = compareTo(lhs.toBigInt())
  override fun compareTo(lhs: UShort) = compareTo(lhs.toBigInt())
  override fun compareTo(lhs: UInt) = compareTo(lhs.toBigInt())
  override fun compareTo(lhs: ULong) = compareTo(lhs.toBigInt())

  override fun compareTo(lhs: BigInt): Int {
    lhs as BigIntImpl

    if (digits.size > lhs.digits.size)
      return 1
    else if (digits.size < lhs.digits.size)
      return -1

    var i = 0
    while (i < digits.size) {
      if (digits[i] > lhs.digits[i])
        return 1
      else if (digits[i] < lhs.digits[i])
        return -1
      else
        i++
    }

    return 0
  }

  override fun unaryMinus(): BigInt = BigIntImpl(!isNegative, digits)

  override fun equals(other: Any?): Boolean {
    if (other === this)
      return true

    if (other is BigIntImpl)
      return digits.contentEquals(other.digits)

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

  override fun toString() = toPlainString()
}