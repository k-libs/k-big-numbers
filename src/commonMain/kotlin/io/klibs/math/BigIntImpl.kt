package io.klibs.math

import io.klibs.collections.UByteDeque

internal class BigIntImpl(
  override val isNegative: Boolean,
  private val digits: UByteDeque,
) : BigInt {

  override val isPositive: Boolean
    get() = !isNegative

  // region Maintenance

  private fun trimToSize() {
    while (digits.isNotEmpty() && digits.peekFirst() == UBYTE_0)
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
    if (digits[0] > UBYTE_1)
      return false
    if (digits[1] > UBYTE_2)
      return false
    if (digits[1] == UBYTE_2) {
      if (isNegative) {
        if (digits[2] > UBYTE_8)
          return false
      } else {
        if (digits[2] > UBYTE_7)
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
    else if (digits[0] > UBYTE_3)
      false
    else if (digits[0] < UBYTE_3)
      true
    else if (digits[1] > UBYTE_2)
      false
    else if (digits[1] < UBYTE_2)
      true
    else if (digits[2] > UBYTE_7)
      false
    else if (digits[2] < UBYTE_7)
      true
    else if (digits[3] > UBYTE_6)
      false
    else if (digits[3] < UBYTE_6)
      true
    else if (isNegative)
      digits[4] <= UBYTE_8
    else
      digits[4] <= UBYTE_7
  }

  override fun fitsInt(): Boolean {
    trimToSize()

    return if (digits.size < 10)
      true
    else if (digits.size > 10)
      false
    else if (digits[0] > UBYTE_2)
      false
    else if (digits[0] < UBYTE_2)
      true
    else if (digits[1] > UBYTE_1)
      false
    else if (digits[1] < UBYTE_1)
      true
    else if (digits[2] > UBYTE_4)
      false
    else if (digits[2] < UBYTE_4)
      true
    else if (digits[3] > UBYTE_7)
      false
    else if (digits[3] < UBYTE_7)
      true
    else if (digits[4] > UBYTE_4)
      false
    else if (digits[4] < UBYTE_4)
      true
    else if (digits[5] > UBYTE_8)
      false
    else if (digits[5] < UBYTE_8)
      true
    else if (digits[6] > UBYTE_3)
      false
    else if (digits[6] < UBYTE_3)
      true
    else if (digits[7] > UBYTE_6)
      false
    else if (digits[7] < UBYTE_6)
      true
    else if (digits[8] > UBYTE_4)
      false
    else if (digits[8] < UBYTE_4)
      true
    else if (isNegative)
      digits[9] <= UBYTE_8
    else
      digits[9] <= UBYTE_7
  }

  override fun fitsLong(): Boolean {
    trimToSize()

    return if (digits.size > 19)
      false
    else if (digits.size < 19)
      true
    else if (digits[0] < UBYTE_9)
      true
    else if (digits[1] > UBYTE_2)
      false
    else if (digits[1] < UBYTE_2)
      true
    else if (digits[2] > UBYTE_2)
      false
    else if (digits[2] < UBYTE_2)
      true
    else if (digits[3] > UBYTE_3)
      false
    else if (digits[3] < UBYTE_3)
      true
    else if (digits[4] > UBYTE_3)
      false
    else if (digits[4] < UBYTE_3)
      true
    else if (digits[5] > UBYTE_7)
      false
    else if (digits[5] < UBYTE_7)
      true
    else if (digits[6] > UBYTE_2)
      false
    else if (digits[6] < UBYTE_2)
      true
    else if (digits[7] > UBYTE_0)
      false
    else if (digits[8] > UBYTE_3)
      false
    else if (digits[8] < UBYTE_3)
      true
    else if (digits[9] > UBYTE_6)
      false
    else if (digits[9] < UBYTE_6)
      true
    else if (digits[10] > UBYTE_8)
      false
    else if (digits[10] < UBYTE_8)
      true
    else if (digits[11] > UBYTE_5)
      false
    else if (digits[11] < UBYTE_5)
      true
    else if (digits[12] > UBYTE_4)
      false
    else if (digits[12] < UBYTE_4)
      true
    else if (digits[13] > UBYTE_7)
      false
    else if (digits[13] < UBYTE_7)
      true
    else if (digits[14] > UBYTE_7)
      false
    else if (digits[14] < UBYTE_7)
      true
    else if (digits[15] > UBYTE_5)
      false
    else if (digits[15] < UBYTE_5)
      true
    else if (digits[16] > UBYTE_8)
      false
    else if (digits[16] < UBYTE_8)
      true
    else if (digits[17] > UBYTE_0)
      false
    else if (isNegative)
      digits[18] <= UBYTE_8
    else
      digits[18] <= UBYTE_7
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
    else if (digits[0] > UBYTE_2)
      false
    else if (digits[0] < UBYTE_2)
      true
    else if (digits[1] > UBYTE_5)
      false
    else if (digits[1] < UBYTE_5)
      true
    else
      digits[2] <= UBYTE_5
  }

  override fun fitsUShort(): Boolean {
    if (isNegative)
      return false

    trimToSize()

    return if (digits.size > 5)
      false
    else if (digits.size < 5)
      true
    else if (digits[0] > UBYTE_6)
      false
    else if (digits[0] > UBYTE_6)
      true
    else if (digits[1] > UBYTE_5)
      false
    else if (digits[1] > UBYTE_5)
      true
    else if (digits[2] > UBYTE_5)
      false
    else if (digits[2] > UBYTE_5)
      true
    else if (digits[3] > UBYTE_3)
      false
    else if (digits[3] > UBYTE_3)
      true
    else
      digits[4] <= UBYTE_5
  }

  override fun fitsUInt(): Boolean {
    if (isNegative)
      return false

    trimToSize()

    return if (digits.size > 10)
      false
    else if (digits.size < 10)
      true
    else if (digits[0] > UBYTE_4)
      false
    else if (digits[0] < UBYTE_4)
      true
    else if (digits[1] > UBYTE_2)
      false
    else if (digits[1] < UBYTE_2)
      true
    else if (digits[2] < UBYTE_9)
      true
    else if (digits[3] > UBYTE_4)
      false
    else if (digits[3] < UBYTE_4)
      true
    else if (digits[4] < UBYTE_9)
      true
    else if (digits[5] > UBYTE_6)
      false
    else if (digits[5] < UBYTE_6)
      true
    else if (digits[6] > UBYTE_7)
      false
    else if (digits[6] < UBYTE_7)
      true
    else if (digits[7] > UBYTE_2)
      false
    else if (digits[7] < UBYTE_2)
      true
    else if (digits[8] < UBYTE_9)
      true
    else
      digits[9] <= UBYTE_5
  }

  override fun fitsULong(): Boolean {
    if (isNegative)
      return false

    trimToSize()

    return if (digits.size > 20)
      false
    else if (digits.size < 20)
      true
    else if (digits[0] > UBYTE_1)
      false
    else if (digits[0] < UBYTE_1)
      true
    else if (digits[1] > UBYTE_8)
      false
    else if (digits[1] < UBYTE_8)
      true
    else if (digits[2] > UBYTE_4)
      false
    else if (digits[2] < UBYTE_4)
      true
    else if (digits[3] > UBYTE_4)
      false
    else if (digits[3] < UBYTE_4)
      true
    else if (digits[4] > UBYTE_6)
      false
    else if (digits[4] < UBYTE_6)
      true
    else if (digits[5] > UBYTE_7)
      false
    else if (digits[5] < UBYTE_7)
      true
    else if (digits[6] > UBYTE_4)
      false
    else if (digits[6] < UBYTE_4)
      true
    else if (digits[7] > UBYTE_4)
      false
    else if (digits[7] < UBYTE_4)
      true
    else if (digits[8] > UBYTE_0)
      false
    else if (digits[9] > UBYTE_7)
      false
    else if (digits[9] < UBYTE_7)
      true
    else if (digits[10] > UBYTE_3)
      false
    else if (digits[10] < UBYTE_3)
      true
    else if (digits[11] > UBYTE_7)
      false
    else if (digits[11] < UBYTE_7)
      true
    else if (digits[12] > UBYTE_0)
      false
    else if (digits[13] < UBYTE_9)
      true
    else if (digits[14] > UBYTE_5)
      false
    else if (digits[14] < UBYTE_5)
      true
    else if (digits[15] > UBYTE_5)
      false
    else if (digits[15] < UBYTE_5)
      true
    else if (digits[16] > UBYTE_1)
      false
    else if (digits[16] < UBYTE_1)
      true
    else if (digits[17] > UBYTE_6)
      false
    else if (digits[17] < UBYTE_6)
      true
    else if (digits[18] > UBYTE_1)
      false
    else if (digits[18] < UBYTE_1)
      true
    else
      digits[19] <= UBYTE_5
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
    digits.peekEach { out = (out * 10u + it).toUByte() }
    return out
  }

  override fun toUShort(): UShort {
    if (!fitsUShort())
      throw NumberCastException()

    var out: UShort = 0u
    digits.peekEach { out = (out * 10u + it).toUShort() }
    return out
  }

  override fun toUInt(): UInt {
    if (!fitsUInt())
      throw NumberCastException()

    var out = 0u
    digits.peekEach { out = out * 10u + it }
    return out
  }

  override fun toULong(): ULong {
    if (!fitsUInt())
      throw NumberCastException()

    var out = 0uL
    digits.peekEach { out = out * 10u + it }
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
    val out = UByteDeque(digits.size)
    var rem = lhs

    var i = digits.lastIndex
    while (i >= 0) {
      var sum = digits[i--] + (rem % 10).toUByte()
      rem /= 10

      if (sum >= 10u) {
        rem++
        sum -= 10u
      }

      out.pushFirst(sum.toUByte())
    }

    out.ensureCapacity(out.size + rem.decStringWidth())

    while (rem > 0) {
      val digitToAdd = (rem % 10).toUByte()
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
    val out = UByteDeque(digits.size)
    var rem = lhs

    var i = digits.lastIndex
    while (i >= 0) {
      var sum = digits[i--] + (rem % 10u).toUByte()
      rem /= 10u

      if (sum >= 10u) {
        rem++
        sum -= 10u
      }

      out.pushFirst(sum.toUByte())
    }

    out.ensureCapacity(out.size + rem.decStringWidth())

    while (rem > 0u) {
      val digitToAdd = (rem % 10u).toUByte()
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
    val out = UByteDeque(max(lhs.digits.size, digits.size))

    var sum: UInt
    var car = 0u

    for (i in out.capacity downTo 1) {
      sum = (digits.getOrZero(i) + lhs.digits.getOrZero(i)) + car
      car = sum / 10u
      out.pushFirst((sum % 10u).toUByte())
    }

    if (car > 0u)
      out.pushFirst(car.toUByte())

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
    TODO()
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
    TODO()
  }

  override fun minus(lhs: BigInt): BigInt {
    TODO("Not yet implemented")
  }

  private fun posMinus(lhs: BigIntImpl): BigInt {
    TODO()
  }

  // endregion Minus

  override fun times(lhs: Byte) = times(lhs.toLong())
  override fun times(lhs: Short) = times(lhs.toLong())
  override fun times(lhs: Int) = times(lhs.toLong())

  override fun times(lhs: Long): BigInt {
    TODO("Not yet implemented")
  }

  override fun times(lhs: UByte) = times(lhs.toULong())
  override fun times(lhs: UShort) = times(lhs.toULong())
  override fun times(lhs: UInt) = times(lhs.toULong())

  override fun times(lhs: ULong): BigInt {
    TODO("Not yet implemented")
  }

  override fun times(lhs: BigInt): BigInt {
    TODO("Not yet implemented")
  }

  override fun div(lhs: Byte): BigInt {
    TODO("Not yet implemented")
  }

  override fun div(lhs: Short): BigInt {
    TODO("Not yet implemented")
  }

  override fun div(lhs: Int): BigInt {
    TODO("Not yet implemented")
  }

  override fun div(lhs: Long): BigInt {
    TODO("Not yet implemented")
  }

  override fun div(lhs: UByte): BigInt {
    TODO("Not yet implemented")
  }

  override fun div(lhs: UShort): BigInt {
    TODO("Not yet implemented")
  }

  override fun div(lhs: UInt): BigInt {
    TODO("Not yet implemented")
  }

  override fun div(lhs: ULong): BigInt {
    TODO("Not yet implemented")
  }

  override fun div(lhs: BigInt): BigInt {
    TODO("Not yet implemented")
  }

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

  override fun toPlainString(): String {
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
}