package io.klibs.math

import io.klibs.collections.ByteDeque


fun bigIntOf(value: Byte): BigInt {
  val out = ByteDeque(value.decStringWidth())
  val neg = value < 0
  var rem = (if (neg) -value else value).toInt()

  while (rem > 0) {
    out.pushFirst((rem % 10).toByte())
    rem /= 10
  }

  out.trimToSize()

  return BigIntImpl(neg, out)
}

fun bigIntOf(value: Short): BigInt {
  val out = ByteDeque(value.decStringWidth())
  val neg = value < 0
  var rem = (if (neg) -value else value).toInt()

  while (rem > 0) {
    out.pushFirst((rem % 10).toByte())
    rem /= 10
  }

  out.trimToSize()

  return BigIntImpl(neg, out)
}

fun bigIntOf(value: Int): BigInt {
  val out = ByteDeque(value.decStringWidth())
  val neg = value < 0
  var rem = if (neg) -value else value

  while (rem > 0) {
    out.pushFirst((rem % 10).toByte())
    rem /= 10
  }

  out.trimToSize()

  return BigIntImpl(neg, out)
}

fun bigIntOf(value: Long): BigInt {
  val out = ByteDeque(value.decStringWidth())
  val neg = value < 0
  var rem = if (neg) -value else value

  while (rem > 0) {
    out.pushFirst((rem % 10).toByte())
    rem /= 10
  }

  out.trimToSize()

  return BigIntImpl(neg, out)
}

fun bigIntOf(value: UByte): BigInt {
  val out = ByteDeque(value.decStringWidth())
  var rem = value.toUInt()

  while (rem > 0u) {
    out.pushFirst((rem % 10u).toByte())
    rem /= 10u
  }

  out.trimToSize()

  return BigIntImpl(false, out)
}

fun bigIntOf(value: UShort): BigInt {
  val out = ByteDeque(value.decStringWidth())
  var rem = value.toUInt()

  while (rem > 0u) {
    out.pushFirst((rem % 10u).toByte())
    rem /= 10u
  }

  out.trimToSize()

  return BigIntImpl(false, out)
}

fun bigIntOf(value: UInt): BigInt {
  val out = ByteDeque(value.decStringWidth())
  var rem = value

  while (rem > 0u) {
    out.pushFirst((rem % 10u).toByte())
    rem /= 10u
  }

  out.trimToSize()

  return BigIntImpl(false, out)
}

fun bigIntOf(value: ULong): BigInt {
  val out = ByteDeque(value.decStringWidth())
  var rem = value

  while (rem > 0u) {
    out.pushFirst((rem % 10u).toByte())
    rem /= 10u
  }

  out.trimToSize()

  return BigIntImpl(false, out)
}

fun bigIntOf(string: String): BigInt {
  if (string.isEmpty())
    throw IllegalArgumentException()

  return bigIntOfBase10(string)
}

private fun bigIntOfBase10(string: String): BigInt {
  val digits = ByteDeque(string.length)
  var started = false
  var negative = false
  var i = 0

  if (string[0] == '-') {
    negative = true
    i++
  }

  while (i < string.length) {
    val c = string[i++]

    if (!c.isBase10Digit())
      throw NumberFormatException()

    if (!started && c == '0')
      continue

    started = true
    digits.pushLast((c.code.toByte() - B_ASCII_ZERO).toByte())
  }

  digits.trimToSize()

  return BigIntImpl(negative, digits)
}