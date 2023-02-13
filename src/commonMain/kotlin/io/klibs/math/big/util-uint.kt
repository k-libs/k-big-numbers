package io.klibs.math.big

internal inline fun bitLengthForUInt(value: UInt) = 32 - leadingZeroBits(value)

internal fun leadingZeroBits(value: UInt): Int {
  if (value == 0u)
    return 32

  var o = 32
  var u = value

  if (u >= 1u shl 16) { o -= 16; u = u shr 16 }
  if (u >= 1u shl  8) { o -=  8; u = u shr  8 }
  if (u >= 1u shl  4) { o -=  4; u = u shr  4 }
  if (u >= 1u shl  2) { o -=  4; u = u shr  2 }

  return o - (u shr 1).toInt()
}

internal fun bitCount(value: UInt): Int {
  var u = value

  u -= (u shr 1) and 0x55555555u
  u = (u and 0x33333333u) + ((u shr 2) and 0x33333333u)
  u = (u + (u shr 4)) and 0x0f0f0f0fu
  u += u shr 8
  u += u shr 16

  return (u and 0x3Fu).toInt()
}