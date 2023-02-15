package io.klibs.math.big

private val bitsPerDigit = byteArrayOf(
  0, 0, 1, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5,
  5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6)

private val digitsPerInt = byteArrayOf(
  0, 0, 30, 19, 15, 13, 11, 11, 10, 9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 7, 7, 7, 6, 6,
  6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 5)

private val intRadix = intArrayOf(
  0, 0, 0x40000000, 0x4546b3db, 0x40000000, 0x48c27395, 0x159fd800, 0x75db9c97,
  0x40000000, 0x17179149, 0x3b9aca00, 0xcc6db61, 0x19a10000, 0x309f1021,
  0x57f6c100, 0xa2f1b6f, 0x10000000, 0x18754571, 0x247dbc80, 0x3547667b,
  0x4c4b4000, 0x6b5a6e1d, 0x6c20a40, 0x8d2d931, 0xb640000, 0xe8d4a51,
  0x1269ae40, 0x17179149, 0x1cb91000, 0x23744899, 0x2b73a840, 0x34e63b41,
  0x40000000, 0x4cfa3cc1, 0x5c13d840, 0x6d91b519, 0x39aa400)

fun bigIntOf(value: String, radix: Int = 10): BigInt {
  val s = value.trim()

  if (s.isBlank())
    throw NumberFormatException("attempted to construct a BigInt value from an empty CharSequence")
  if (radix < 2 || radix > 36)
    throw NumberFormatException("radix value ($radix) is not in the valid range of 2..36")

  val numDigits: Int
  var i = 0
  val len = s.length

  var sign: Byte = 1
  val si1 = s.lastIndexOf('-')
  val si2 = s.lastIndexOf('+')

  if (si1 >= 0) {
    if (si1 != 0)
      throw NumberFormatException("illegal embedded sign character")

    sign = -1
    i++
  } else if (si2 >= 0) {
    if (si2 != 0)
      throw NumberFormatException("illegal embedded sign character")

    i++
  }

  if (i == len)
    throw NumberFormatException("attempted to construct a BigInt value from a CharSequence containing only a sign indicator")

  // Skip leading zeros
  while (i < len && s[i].digitToInt(radix) == 0)
    i++

  // if it was all zeros then ignore the sign and return a zero
  if (i == len)
    return BigInt.Zero

  numDigits = len - i
  val numBits: Long = numDigits.toLong() * bitsPerDigit[radix]

  if (numBits + 31 >= 1L shl 32)
    reportOverflow()

  val numWords: Int = ((numBits + 31) ushr 5).toInt()
  var magnitude = IntArray(numWords)

  var firstGroupLen = numDigits % digitsPerInt[radix]
  if (firstGroupLen == 0)
    firstGroupLen = digitsPerInt[radix].toInt()

  // Process first digit group (possibly a short one)
  var group = s.substring(i, i + firstGroupLen)
  i += firstGroupLen

  magnitude[numWords - 1] = group.toInt(radix)
  if (magnitude[numWords - 1] < 0)
    throw NumberFormatException("illegal digit")

  // Process remaining digit groups
  val superRadix = intRadix[radix]
  var groupVal = 0
  while (i < len) {
    group = s.substring(i, i + digitsPerInt[radix])
    i += digitsPerInt[radix]

    groupVal = group.toInt(radix)
    if (groupVal < 0)
      throw NumberFormatException("illegal digit")

    destructiveMulAdd(magnitude, superRadix, groupVal)
  }

  magnitude = trustedStripLeadingZeroInts(magnitude)
  checkRange(magnitude)

  return BigIntImpl(sign, magnitude)
}

fun String.toBigInt(radix: Int = 10) = bigIntOf(this, radix)

private fun destructiveMulAdd(x: IntArray, y: Int, z: Int) {
  val yLong = y.toLong() and LONG_MASK
  val zLong = z.toLong() and LONG_MASK
  val len   = x.size

  var product: Long
  var carry = 0L

  for (i in len - 1 downTo 0) {
    product = yLong * (x[i].toLong() and LONG_MASK) + carry
    x[i] = product.toInt()
    carry = product ushr 32
  }

  var sum = (x[len-1].toLong() and LONG_MASK) + zLong
  x[len-1] = sum.toInt()
  carry = sum ushr 32

  for (i in len - 2 downTo 0) {
    sum = (x[i].toLong() and LONG_MASK) + carry
    x[i] = sum.toInt()
    carry = sum ushr 32
  }
}