package io.klibs.math.big

import io.klibs.collections.ByteDeque

fun bigIntOf(b: Byte): BigInt {
  if (b == B_0) return BigInt.Zero

  val out = ByteDeque(b.decStringWidth())
  var rem = b.toInt()

  while (rem != 0) {
    out.pushFirst(abs((rem % 10).toByte()))
    rem /= 10
  }

  out.trimToSize()

  return ImmutableBigInt(MutableBigIntImpl(when { b < 0 -> -1; b > 0 -> 1; else -> 0 }, out))
}

fun mutableBigIntOf(b: Byte): MutableBigInt {
  if (b == B_0) return MutableBigInt.zero()

  val out = ByteDeque(b.decStringWidth())
  var rem = b.toInt()

  while (rem != 0) {
    out.pushFirst(abs((rem % 10).toByte()))
    rem /= 10
  }

  out.trimToSize()

  return MutableBigIntImpl(when { b < 0 -> -1; b > 0 -> 1; else -> 0 }, out)
}

fun bigIntOf(s: Short): BigInt {
  if (s == 0.toShort()) return BigInt.Zero

  val out = ByteDeque(s.decStringWidth())
  var rem = s.toInt()

  while (rem != 0) {
    out.pushFirst(abs((rem % 10).toByte()))
    rem /= 10
  }

  out.trimToSize()

  return ImmutableBigInt(MutableBigIntImpl(when { s < 0 -> -1; s > 0 -> 1; else -> 0 }, out))
}

fun mutableBigIntOf(s: Short): MutableBigInt {
  if (s == 0.toShort()) return MutableBigInt.zero()

  val out = ByteDeque(s.decStringWidth())
  var rem = s.toInt()

  while (rem != 0) {
    out.pushFirst(abs((rem % 10).toByte()))
    rem /= 10
  }

  out.trimToSize()

  return MutableBigIntImpl(when { s < 0 -> -1; s > 0 -> 1; else -> 0 }, out)
}

fun bigIntOf(i: Int): BigInt {
  if (i == 0) return BigInt.Zero

  val out = ByteDeque(i.decStringWidth())
  var rem = i

  while (rem != 0) {
    out.pushFirst(abs((rem % 10).toByte()))
    rem /= 10
  }

  out.trimToSize()

  return ImmutableBigInt(MutableBigIntImpl(when { i < 0 -> -1; i > 0 -> 1; else -> 0 }, out))
}

fun mutableBigIntOf(i: Int): MutableBigInt {
  if (i == 0) return MutableBigInt.zero()

  val out = ByteDeque(i.decStringWidth())
  var rem = i

  while (rem != 0) {
    out.pushFirst(abs((rem % 10).toByte()))
    rem /= 10
  }

  out.trimToSize()

  return MutableBigIntImpl(when { i < 0 -> -1; i > 0 -> 1; else -> 0 }, out)
}

fun bigIntOf(l: Long): BigInt {
  if (l == 0L) return BigInt.Zero

  val out = ByteDeque(l.decStringWidth())
  var rem = l

  while (rem != 0L) {
    out.pushFirst(abs((rem % 10).toByte()))
    rem /= 10
  }

  out.trimToSize()

  return ImmutableBigInt(MutableBigIntImpl(when { l < 0 -> -1; l > 0 -> 1; else -> 0 }, out))
}

fun mutableBigIntOf(l: Long): MutableBigInt {
  if (l == 0L) return MutableBigInt.zero()

  val out = ByteDeque(l.decStringWidth())
  var rem = l

  while (rem != 0L) {
    out.pushFirst(abs((rem % 10).toByte()))
    rem /= 10
  }

  out.trimToSize()

  return MutableBigIntImpl(when { l < 0 -> -1; l > 0 -> 1; else -> 0 }, out)
}

fun bigIntOf(s: String, radix: BigIntRadix = BigIntRadix.Ten): BigInt {
  return when (radix) {
    BigIntRadix.Ten -> ImmutableBigInt(mutableBigIntOfBase10(s))
  }
}

fun mutableBigIntOf(s: String, radix: BigIntRadix = BigIntRadix.Ten): MutableBigInt {
  return when (radix) {
    BigIntRadix.Ten -> mutableBigIntOfBase10(s)
  }
}

private fun mutableBigIntOfBase10(s: String): MutableBigInt {
  val digits = ByteDeque(s.length)
  var started = false
  var negative = false
  var i = 0

  if (s[0] == '-') {
    negative = true
    i++
  }

  while (i < s.length) {
    val c = s[i++]

    if (!c.isBase10Digit())
      throw NumberFormatException()

    if (!started && c == '0')
      continue

    started = true
    digits.pushLast((c.code.toByte() - B_ASCII_ZERO).toByte())
  }

  digits.trimToSize()

  return MutableBigIntImpl(
    when {
      digits.isEmpty() -> 0
      negative         -> -1
      else             -> 1
    },
    digits
  )
}
