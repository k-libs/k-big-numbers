package io.klibs.math

import io.klibs.collections.UByteDeque

fun bigIntOf(string: String): BigInt {
  if (string.isEmpty())
    throw IllegalArgumentException()

  return bigIntOfBase10(string)
}

private fun bigIntOfBase10(string: String): BigInt {
  val digits = UByteDeque(string.length)
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
    digits.pushLast((c.code.toUByte() - U_ASCII_ZERO).toUByte())
  }

  digits.trimToSize()

  return BigIntImpl(negative, digits)
}