package io.klibs.math

internal class BigIntImpl(private val digits: UByteDigits) : BigInt {

  override fun plus(lhs: Byte): BigInt = plus(lhs.toLong())
  override fun plus(lhs: Short): BigInt = plus(lhs.toLong())
  override fun plus(lhs: Int): BigInt = plus(lhs.toLong())

  override fun plus(lhs: Long): BigInt {
    if (lhs < 0L)
      return minus(-lhs)
    if (lhs == 0L)
      return this

    val out = UByteDigits(digits.size)
    var rem = lhs

    var i = digits.lastIndex
    while (i > 0) {
      val dig = digits[i]
      val mod = (lhs % 10).toUByte()
      var sum = dig + mod
      rem /= 10

      if (sum > 10u) {
        rem++
        sum -= 10u
      }

      digits[i--] = sum.toUByte()
    }

    while (rem > 0) {
      val digitToAdd = (rem % 10).toUByte()
      out.pushHead(digitToAdd)
      rem /= 10
    }

    out.trim()

    return BigIntImpl(out)
  }

  override fun plus(lhs: UByte): BigInt {
    TODO("Not yet implemented")
  }

  override fun plus(lhs: UShort): BigInt {
    TODO("Not yet implemented")
  }

  override fun plus(lhs: UInt): BigInt {
    TODO("Not yet implemented")
  }

  override fun plus(lhs: ULong): BigInt {
    TODO("Not yet implemented")
  }

  override fun plus(lhs: BigInt): BigInt {
    TODO("Not yet implemented")
  }

  override fun minus(lhs: Byte): BigInt {
    TODO("Not yet implemented")
  }

  override fun minus(lhs: Short): BigInt {
    TODO("Not yet implemented")
  }

  override fun minus(lhs: Int): BigInt {
    TODO("Not yet implemented")
  }

  override fun minus(lhs: Long): BigInt {
    TODO("Not yet implemented")
  }

  override fun minus(lhs: UByte): BigInt {
    TODO("Not yet implemented")
  }

  override fun minus(lhs: UShort): BigInt {
    TODO("Not yet implemented")
  }

  override fun minus(lhs: UInt): BigInt {
    TODO("Not yet implemented")
  }

  override fun minus(lhs: ULong): BigInt {
    TODO("Not yet implemented")
  }

  override fun minus(lhs: BigInt): BigInt {
    TODO("Not yet implemented")
  }

  override fun times(lhs: Byte): BigInt {
    TODO("Not yet implemented")
  }

  override fun times(lhs: Short): BigInt {
    TODO("Not yet implemented")
  }

  override fun times(lhs: Int): BigInt {
    TODO("Not yet implemented")
  }

  override fun times(lhs: Long): BigInt {
    TODO("Not yet implemented")
  }

  override fun times(lhs: UByte): BigInt {
    TODO("Not yet implemented")
  }

  override fun times(lhs: UShort): BigInt {
    TODO("Not yet implemented")
  }

  override fun times(lhs: UInt): BigInt {
    TODO("Not yet implemented")
  }

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

  override fun toPlainString(): String {
    val 
  }
}