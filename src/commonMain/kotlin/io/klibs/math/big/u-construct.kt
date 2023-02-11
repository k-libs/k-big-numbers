package io.klibs.math.big

import io.klibs.collections.ByteDeque

fun bigIntOf(b: UByte): BigInt {
  if (b == 0.toUByte()) return BigInt.Zero

  val out = ByteDeque(b.decStringWidth())
  var rem = b.toUInt()

  while (rem != 0u) {
    out.pushFirst(abs((rem % 10u).toByte()))
    rem /= 10u
  }

  out.trimToSize()

  return ImmutableBigInt(MutableBigIntImpl(when { b < 0u -> -1; b > 0u -> 1; else -> 0 }, out))
}

fun mutableBigIntOf(b: UByte): MutableBigInt {
  if (b == 0.toUByte()) return MutableBigInt.zero()

  val out = ByteDeque(b.decStringWidth())
  var rem = b.toUInt()

  while (rem != 0u) {
    out.pushFirst(abs((rem % 10u).toByte()))
    rem /= 10u
  }

  out.trimToSize()

  return MutableBigIntImpl(when { b < 0u -> -1; b > 0u -> 1; else -> 0 }, out)
}

fun bigIntOf(s: UShort): BigInt {
  if (s == 0.toUShort()) return BigInt.Zero

  val out = ByteDeque(s.decStringWidth())
  var rem = s.toUInt()

  while (rem != 0u) {
    out.pushFirst(abs((rem % 10u).toByte()))
    rem /= 10u
  }

  out.trimToSize()

  return ImmutableBigInt(MutableBigIntImpl(when { s < 0u -> -1; s > 0u -> 1; else -> 0 }, out))
}

fun mutableBigIntOf(s: UShort): MutableBigInt {
  if (s == 0.toUShort()) return MutableBigInt.zero()

  val out = ByteDeque(s.decStringWidth())
  var rem = s.toUInt()

  while (rem != 0u) {
    out.pushFirst(abs((rem % 10u).toByte()))
    rem /= 10u
  }

  out.trimToSize()

  return MutableBigIntImpl(when { s < 0u -> -1; s > 0u -> 1; else -> 0 }, out)
}

fun bigIntOf(i: UInt): BigInt {
  if (i == 0u) return BigInt.Zero

  val out = ByteDeque(i.decStringWidth())
  var rem = i

  while (rem != 0u) {
    out.pushFirst(abs((rem % 10u).toByte()))
    rem /= 10u
  }

  out.trimToSize()

  return ImmutableBigInt(MutableBigIntImpl(when { i < 0u -> -1; i > 0u -> 1; else -> 0 }, out))
}

fun mutableBigIntOf(i: UInt): MutableBigInt {
  if (i == 0u) return MutableBigInt.zero()

  val out = ByteDeque(i.decStringWidth())
  var rem = i

  while (rem != 0u) {
    out.pushFirst(abs((rem % 10u).toByte()))
    rem /= 10u
  }

  out.trimToSize()

  return MutableBigIntImpl(when { i < 0u -> -1; i > 0u -> 1; else -> 0 }, out)
}

fun bigIntOf(l: ULong): BigInt {
  if (l == 0uL) return BigInt.Zero

  val out = ByteDeque(l.decStringWidth())
  var rem = l

  while (rem != 0uL) {
    out.pushFirst(abs((rem % 10u).toByte()))
    rem /= 10u
  }

  out.trimToSize()

  return ImmutableBigInt(MutableBigIntImpl(when { l < 0u -> -1; l > 0u -> 1; else -> 0 }, out))
}

fun mutableBigIntOf(l: ULong): MutableBigInt {
  if (l == 0uL) return MutableBigInt.zero()

  val out = ByteDeque(l.decStringWidth())
  var rem = l

  while (rem != 0uL) {
    out.pushFirst(abs((rem % 10u).toByte()))
    rem /= 10u
  }

  out.trimToSize()

  return MutableBigIntImpl(when { l < 0u -> -1; l > 0u -> 1; else -> 0 }, out)
}
