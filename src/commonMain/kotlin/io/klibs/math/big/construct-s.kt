package io.klibs.math.big

import io.klibs.collections.uintDequeOf

@OptIn(ExperimentalUnsignedTypes::class)
fun bigIntOf(b: Byte): BigInt {
  return when {
    b < 0 -> ImmutableBigInt(MutableBigIntImpl(-1, uintDequeOf((-b).toUInt())))
    b > 0 -> ImmutableBigInt(MutableBigIntImpl(1, uintDequeOf(b.toUInt())))
    else  -> BigInt.Zero
  }
}

@OptIn(ExperimentalUnsignedTypes::class)
fun mutableBigIntOf(b: Byte): MutableBigInt {
  return when {
    b < 0 -> MutableBigIntImpl(-1, uintDequeOf((-b).toUInt()))
    b > 0 -> MutableBigIntImpl(1, uintDequeOf(b.toUInt()))
    else  -> MutableBigInt.zero()
  }
}

@OptIn(ExperimentalUnsignedTypes::class)
fun bigIntOf(s: Short): BigInt {
  return when {
    s < 0 -> ImmutableBigInt(MutableBigIntImpl(-1, uintDequeOf((-s).toUInt())))
    s > 0 -> ImmutableBigInt(MutableBigIntImpl(1, uintDequeOf(s.toUInt())))
    else  -> BigInt.Zero
  }
}

@OptIn(ExperimentalUnsignedTypes::class)
fun mutableBigIntOf(s: Short): MutableBigInt {
  return when {
    s < 0 -> MutableBigIntImpl(-1, uintDequeOf((-s).toUInt()))
    s > 0 -> MutableBigIntImpl(1, uintDequeOf(s.toUInt()))
    else  -> MutableBigInt.zero()
  }
}

@OptIn(ExperimentalUnsignedTypes::class)
fun bigIntOf(i: Int): BigInt {
  return when {
    i < 0 -> ImmutableBigInt(MutableBigIntImpl(-1, uintDequeOf((-(i.toLong())).toUInt())))
    i > 0 -> ImmutableBigInt(MutableBigIntImpl(1, uintDequeOf(i.toUInt())))
    else  -> BigInt.Zero
  }
}

@OptIn(ExperimentalUnsignedTypes::class)
fun mutableBigIntOf(i: Int): MutableBigInt {
  return when {
    i < 0 -> MutableBigIntImpl(-1, uintDequeOf((-(i.toLong())).toUInt()))
    i > 0 -> MutableBigIntImpl(1, uintDequeOf(i.toUInt()))
    else  -> MutableBigInt.zero()
  }
}

fun bigIntOf(l: Long): BigInt {
  if (l == 0L)
    return BigInt.Zero

  val neg = l < 0
  return ImmutableBigInt(internalBigIntOfLong(neg, if (neg) (-l).toULong() else l.toULong()))
}

fun mutableBigIntOf(l: Long): MutableBigInt {
  if (l == 0L)
    return MutableBigInt.zero()

  val neg = l < 0
  return internalBigIntOfLong(neg, if (neg) (-l).toULong() else l.toULong())
}

@OptIn(ExperimentalUnsignedTypes::class)
private fun internalBigIntOfLong(neg: Boolean, l: ULong): MutableBigInt {
  return if (l > 0xFFFFFFFFu) {
    MutableBigIntImpl(if (neg) -1 else 1, uintDequeOf((l shr 32).toUInt(), (l and 0xFFFFFFFFu).toUInt()))
  } else {
    MutableBigIntImpl(if (neg) -1 else 1, uintDequeOf(l.toUInt()))
  }
}
