package io.klibs.math.big

import io.klibs.collections.uintDequeOf

@OptIn(ExperimentalUnsignedTypes::class)
fun bigIntOf(b: UByte): BigInt {
  return if (b == 0.toUByte())
    BigInt.Zero
  else
    ImmutableBigInt(MutableBigIntImpl(1, uintDequeOf(b.toUInt())))
}

@OptIn(ExperimentalUnsignedTypes::class)
fun mutableBigIntOf(b: UByte): MutableBigInt {
  return if (b == 0.toUByte())
    MutableBigInt.zero()
  else
    MutableBigIntImpl(1, uintDequeOf(b.toUInt()))
}

@OptIn(ExperimentalUnsignedTypes::class)
fun bigIntOf(s: UShort): BigInt {
  return if (s == 0.toUShort())
    BigInt.Zero
  else
    ImmutableBigInt(MutableBigIntImpl(1, uintDequeOf(s.toUInt())))
}

@OptIn(ExperimentalUnsignedTypes::class)
fun mutableBigIntOf(s: UShort): MutableBigInt {
  return if (s == 0.toUShort())
    MutableBigInt.zero()
  else
    MutableBigIntImpl(1, uintDequeOf(s.toUInt()))
}

@OptIn(ExperimentalUnsignedTypes::class)
fun bigIntOf(i: UInt): BigInt {
  return if (i == 0u)
    BigInt.Zero
  else
    ImmutableBigInt(MutableBigIntImpl(1, uintDequeOf(i)))
}

@OptIn(ExperimentalUnsignedTypes::class)
fun mutableBigIntOf(i: UInt): MutableBigInt {
  return if (i == 0u)
    MutableBigInt.zero()
  else
    MutableBigIntImpl(1, uintDequeOf(i))
}

@OptIn(ExperimentalUnsignedTypes::class)
fun bigIntOf(l: ULong): BigInt {
  return if (l == 0uL)
    BigInt.Zero
  else if (l < 0xFFFFFFFFu)
    ImmutableBigInt(MutableBigIntImpl(1, uintDequeOf(l.toUInt())))
  else
    ImmutableBigInt(MutableBigIntImpl(1, uintDequeOf((l shr 32).toUInt(), l.toUInt())))
}

@OptIn(ExperimentalUnsignedTypes::class)
fun mutableBigIntOf(l: ULong): MutableBigInt {
  return if (l == 0uL)
    MutableBigInt.zero()
  else if (l < 0xFFFFFFFFu)
    MutableBigIntImpl(1, uintDequeOf(l.toUInt()))
  else
    MutableBigIntImpl(1, uintDequeOf((l shr 32).toUInt(), l.toUInt()))
}
