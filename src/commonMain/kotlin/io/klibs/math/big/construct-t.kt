package io.klibs.math.big

import io.klibs.collections.UIntDeque

fun bigIntOf(s: String, radix: BigIntRadix): BigInt {
  if (s.isBlank())
    throw NumberFormatException("cannot parse a number from a blank string")

  var i = 0
  val l = s.length

  val neg = when(s[i]) {
    '-'  -> { i++; true }
    '+'  -> { i++; false }
    else -> { false }
  }

  // If we are now at the end of the string, then the whole thing was just a
  // pos/neg indicator and that's just not right.
  if (i == l)
    throw NumberFormatException("invalid number string")

  // Skip any leading zeros
  while (i < l && s[i] == '0')
    i++

  // If we're at the end of the input string, then it was zeros all the way down
  if (i == l)
    return MutableBigInt.zero()

  return ImmutableBigInt(internalMutableBigIntOf(neg, i, l, s, radix))
}

fun mutableBigIntOf(s: String, radix: BigIntRadix): MutableBigInt {
  if (s.isBlank())
    throw NumberFormatException("cannot parse a number from a blank string")

  var i = 0
  val l = s.length

  val neg = when(s[i]) {
    '-'  -> { i++; true }
    '+'  -> { i++; false }
    else -> { false }
  }

  // If we are now at the end of the string, then the whole thing was just a
  // pos/neg indicator and that's just not right.
  if (i == l)
    throw NumberFormatException("invalid number string")

  // Skip any leading zeros
  while (i < l && s[i] == '0')
    i++

  // If we're at the end of the input string, then it was zeros all the way down
  if (i == l)
    return MutableBigInt.zero()

  return internalMutableBigIntOf(neg, i, l, s, radix)
}

private fun internalMutableBigIntOf(n: Boolean, ib: Int, l: Int, s: String, r: BigIntRadix): MutableBigInt {
  val digitCount = l - ib
  val bitCount = digitCount.toLong() * r.bitsPerDigit
  val chunks = UIntDeque(((bitCount + 31) ushr 5).toInt())

  // TODO: if this was done in chunks it would be faster as we wouldn't be multiplying per digit

  var i = ib
  while (i < l)
    multiplyThenAdd(chunks, r.value, r.charToDigit(s[i++]))

  return MutableBigIntImpl(if (n) -1 else 1, chunks)
}

private fun multiplyThenAdd(chunks: UIntDeque, multiplicand: UInt, addend: UInt) {
  val lMultiplicand = multiplicand.toULong()

  var product: ULong
  var sum: ULong
  var carry = 0uL

  // Do the multiply
  for (i in chunks.lastIndex downTo 0) {
    product = lMultiplicand * chunks[i].toULong() + carry
    chunks[i] = product.toUInt()
    carry = product shr 32
  }

  carry = addend.toULong()

  // Do tha add
  for (i in chunks.lastIndex downTo 0) {
    sum = chunks[i].toULong() + carry
    chunks[i] = sum.toUInt()
    carry = sum shr 32
  }
}
