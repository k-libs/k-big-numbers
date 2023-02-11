package io.klibs.math.big

import io.klibs.collections.intDequeOf
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

  val low = l % L_M


}

fun mutableBigIntOf(l: Long): MutableBigInt {
  if (l == 0L)
    return MutableBigInt.zero()


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

private fun mutableBigIntOfBase10(s: String): MutableBigInt {}
