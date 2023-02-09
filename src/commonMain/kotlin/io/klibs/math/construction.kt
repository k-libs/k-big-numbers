package io.klibs.math

import io.klibs.collections.ByteDeque

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