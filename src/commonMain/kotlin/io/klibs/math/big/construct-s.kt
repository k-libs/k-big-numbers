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

fun mutableBigIntOf(b: Byte): MutableBigInt {
  return when {
    b < 0 -> MutableBigIntImpl(-1, intDequeOf(-b))
    b > 0 -> MutableBigIntImpl(1, intDequeOf(b.toInt()))
    else  -> MutableBigInt.zero()
  }
}

fun bigIntOf(s: Short): BigInt {
  return when {
    s < 0 -> ImmutableBigInt(MutableBigIntImpl(-1, intDequeOf(-s)))
    s > 0 -> ImmutableBigInt(MutableBigIntImpl(1, intDequeOf(s.toInt())))
    else  -> BigInt.Zero
  }
}

fun mutableBigIntOf(s: Short): MutableBigInt {
  return when {
    s < 0 -> MutableBigIntImpl(-1, intDequeOf(-s))
    s > 0 -> MutableBigIntImpl(1, intDequeOf(s.toInt()))
    else  -> MutableBigInt.zero()
  }
}

fun bigIntOf(i: Int): BigInt {
  return when {
    i < 0 -> ImmutableBigInt(MutableBigIntImpl(-1, intDequeOf(i)))
    i > 0 -> ImmutableBigInt(MutableBigIntImpl(1, intDequeOf(i)))
    else  -> BigInt.Zero
  }
}

fun mutableBigIntOf(i: Int): MutableBigInt {
  return when {
    i < 0 -> MutableBigIntImpl(-1, intDequeOf(i))
    i > 0 -> MutableBigIntImpl(1, intDequeOf(i))
    else  -> MutableBigInt.zero()
  }
}

fun bigIntOf(l: Long): BigInt {
  return when {
    l > 0 -> {
      if (l > L_I_MAX) {
        ImmutableBigInt(MutableBigIntImpl(1, intDequeOf((l ushr 32).toInt(), (l and L_M).toInt())))
      } else {
        bigIntOf(l.toInt())
      }
    }
    l < 0 -> {
      if (l < L_I_MIN) {
        ImmutableBigInt(MutableBigIntImpl(-1, intDequeOf((l ushr 32).toInt(), (l and L_M).toInt())))
      } else {
        bigIntOf(l.toInt())
      }
    }
    else  -> BigInt.Zero
  }
}

fun mutableBigIntOf(l: Long): MutableBigInt {}

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
