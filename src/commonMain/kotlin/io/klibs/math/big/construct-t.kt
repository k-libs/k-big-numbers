package io.klibs.math.big

import io.klibs.collections.UIntDeque

fun bigIntOf(s: String, radix: BigIntRadix): BigInt = ImmutableBigInt(mutableBigIntOf(s, radix))

fun mutableBigIntOf(s: String, radix: BigIntRadix): MutableBigInt {
  if (s.isBlank())
    throw NumberFormatException("cannot parse a BigInt instance from a blank string")

  return when (radix) {
    BigIntRadix.Ten -> mutableBigIntOfBase10(s.trim())
  }
}

private fun mutableBigIntOfBase10(s: String): MutableBigInt {
  var i = 0
  val l = s.length

  val neg = if (s[0] == '-') {
    i++
    true
  } else if (s[0] == '+') {
    i++
    false
  } else {
    false
  }

  // Skip leading zeros
  while (i < l && s[i] == '0')
    i++

  // If we ate the whole string while skipping zeros, then the whole thing was
  // zero.
  if (i == l)
    return MutableBigInt.zero()

  val numDigits = l - i
  val buffer =  UIntDeque(numDigits / 8 + 1)
  var holder = 0uL

  while (i < l) {
    val c = s[i++]

    if (!c.isBase10Digit())
      throw NumberFormatException("illegal character in number string")

    holder *= 10u
    holder += c.toBase10Digit().toULong()

    if (holder >= 0xFFFFFFFFu) {
      buffer.prepend(holder.toUInt())
      holder = holder shr 32
    }
  }

  if (holder > 0u)
    buffer.prepend(holder.toUInt())

  return MutableBigIntImpl(if (neg) -1 else 1, buffer)
}
