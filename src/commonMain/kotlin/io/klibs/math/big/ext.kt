package io.klibs.math.big

inline fun Byte.toBigInt(): BigInt = bigIntOf(this)
inline fun Byte.toMutableBigInt(): MutableBigInt = mutableBigIntOf(this)

inline fun Short.toBigInt(): BigInt = bigIntOf(this)
inline fun Short.toMutableBigInt(): MutableBigInt = mutableBigIntOf(this)

inline fun Int.toBigInt(): BigInt = bigIntOf(this)
inline fun Int.toMutableBigInt(): MutableBigInt = mutableBigIntOf(this)

inline fun Long.toBigInt(): BigInt = bigIntOf(this)
inline fun Long.toMutableBigInt(): MutableBigInt = mutableBigIntOf(this)

inline fun String.toBigInt(radix: BigIntRadix = BigIntRadix.Ten): BigInt = bigIntOf(this, radix)
inline fun String.toMutableBigInt(radix: BigIntRadix = BigIntRadix.Ten): MutableBigInt = mutableBigIntOf(this, radix)

operator fun BigInt.plus(rhs: Byte): BigInt = plus(rhs.toBigInt())
operator fun BigInt.plus(rhs: Short): BigInt = plus(rhs.toBigInt())
operator fun BigInt.plus(rhs: Int): BigInt = plus(rhs.toBigInt())
operator fun BigInt.plus(rhs: Long): BigInt = plus(rhs.toBigInt())

operator fun MutableBigInt.plus(rhs: Byte): MutableBigInt = plus(rhs.toMutableBigInt())
operator fun MutableBigInt.plus(rhs: Short): MutableBigInt = plus(rhs.toMutableBigInt())
operator fun MutableBigInt.plus(rhs: Int): MutableBigInt = plus(rhs.toMutableBigInt())
operator fun MutableBigInt.plus(rhs: Long): MutableBigInt = plus(rhs.toMutableBigInt())

operator fun MutableBigInt.plusAssign(rhs: Byte) = plusAssign(rhs.toMutableBigInt())
operator fun MutableBigInt.plusAssign(rhs: Short) = plusAssign(rhs.toMutableBigInt())
operator fun MutableBigInt.plusAssign(rhs: Int) = plusAssign(rhs.toMutableBigInt())
operator fun MutableBigInt.plusAssign(rhs: Long) = plusAssign(rhs.toMutableBigInt())

operator fun BigInt.minus(rhs: Byte): BigInt = minus(rhs.toBigInt())
operator fun BigInt.minus(rhs: Short): BigInt = minus(rhs.toBigInt())
operator fun BigInt.minus(rhs: Int): BigInt = minus(rhs.toBigInt())
operator fun BigInt.minus(rhs: Long): BigInt = minus(rhs.toBigInt())

operator fun MutableBigInt.minus(rhs: Byte): MutableBigInt = minus(rhs.toMutableBigInt())
operator fun MutableBigInt.minus(rhs: Short): MutableBigInt = minus(rhs.toMutableBigInt())
operator fun MutableBigInt.minus(rhs: Int): MutableBigInt = minus(rhs.toMutableBigInt())
operator fun MutableBigInt.minus(rhs: Long): MutableBigInt = minus(rhs.toMutableBigInt())

operator fun MutableBigInt.minusAssign(rhs: Byte) = minusAssign(rhs.toMutableBigInt())
operator fun MutableBigInt.minusAssign(rhs: Short) = minusAssign(rhs.toMutableBigInt())
operator fun MutableBigInt.minusAssign(rhs: Int) = minusAssign(rhs.toMutableBigInt())
operator fun MutableBigInt.minusAssign(rhs: Long) = minusAssign(rhs.toMutableBigInt())

operator fun BigInt.times(rhs: Byte): BigInt = times(rhs.toBigInt())
operator fun BigInt.times(rhs: Short): BigInt = times(rhs.toBigInt())
operator fun BigInt.times(rhs: Int): BigInt = times(rhs.toBigInt())
operator fun BigInt.times(rhs: Long): BigInt = times(rhs.toBigInt())

operator fun MutableBigInt.times(rhs: Byte): MutableBigInt = times(rhs.toMutableBigInt())
operator fun MutableBigInt.times(rhs: Short): MutableBigInt = times(rhs.toMutableBigInt())
operator fun MutableBigInt.times(rhs: Int): MutableBigInt = times(rhs.toMutableBigInt())
operator fun MutableBigInt.times(rhs: Long): MutableBigInt = times(rhs.toMutableBigInt())

operator fun MutableBigInt.timesAssign(rhs: Byte) = timesAssign(rhs.toMutableBigInt())
operator fun MutableBigInt.timesAssign(rhs: Short) = timesAssign(rhs.toMutableBigInt())
operator fun MutableBigInt.timesAssign(rhs: Int) = timesAssign(rhs.toMutableBigInt())
operator fun MutableBigInt.timesAssign(rhs: Long) = timesAssign(rhs.toMutableBigInt())
//
//operator fun BigInt.div(rhs: Byte): BigInt = div(rhs.toBigInt())
//operator fun BigInt.div(rhs: Short): BigInt = div(rhs.toBigInt())
//operator fun BigInt.div(rhs: Int): BigInt = div(rhs.toBigInt())
//operator fun BigInt.div(rhs: Long): BigInt = div(rhs.toBigInt())
//
//operator fun MutableBigInt.div(rhs: Byte): MutableBigInt = div(rhs.toMutableBigInt())
//operator fun MutableBigInt.div(rhs: Short): MutableBigInt = div(rhs.toMutableBigInt())
//operator fun MutableBigInt.div(rhs: Int): MutableBigInt = div(rhs.toMutableBigInt())
//operator fun MutableBigInt.div(rhs: Long): MutableBigInt = div(rhs.toMutableBigInt())
//
//operator fun MutableBigInt.divAssign(rhs: Byte) = divAssign(rhs.toMutableBigInt())
//operator fun MutableBigInt.divAssign(rhs: Short) = divAssign(rhs.toMutableBigInt())
//operator fun MutableBigInt.divAssign(rhs: Int) = divAssign(rhs.toMutableBigInt())
//operator fun MutableBigInt.divAssign(rhs: Long) = divAssign(rhs.toMutableBigInt())
//
//operator fun BigInt.rem(rhs: Byte): BigInt = rem(rhs.toBigInt())
//operator fun BigInt.rem(rhs: Short): BigInt = rem(rhs.toBigInt())
//operator fun BigInt.rem(rhs: Int): BigInt = rem(rhs.toBigInt())
//operator fun BigInt.rem(rhs: Long): BigInt = rem(rhs.toBigInt())
//
//operator fun MutableBigInt.rem(rhs: Byte): MutableBigInt = rem(rhs.toMutableBigInt())
//operator fun MutableBigInt.rem(rhs: Short): MutableBigInt = rem(rhs.toMutableBigInt())
//operator fun MutableBigInt.rem(rhs: Int): MutableBigInt = rem(rhs.toMutableBigInt())
//operator fun MutableBigInt.rem(rhs: Long): MutableBigInt = rem(rhs.toMutableBigInt())
//
//operator fun MutableBigInt.remAssign(rhs: Byte) = remAssign(rhs.toMutableBigInt())
//operator fun MutableBigInt.remAssign(rhs: Short) = remAssign(rhs.toMutableBigInt())
//operator fun MutableBigInt.remAssign(rhs: Int) = remAssign(rhs.toMutableBigInt())
//operator fun MutableBigInt.remAssign(rhs: Long) = remAssign(rhs.toMutableBigInt())
