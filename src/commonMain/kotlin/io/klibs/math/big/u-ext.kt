package io.klibs.math.big


fun UByte.toBigInt(): BigInt = bigIntOf(this)
fun UByte.toMutableBigInt(): MutableBigInt = mutableBigIntOf(this)

fun UShort.toBigInt(): BigInt = bigIntOf(this)
fun UShort.toMutableBigInt(): MutableBigInt = mutableBigIntOf(this)

fun UInt.toBigInt(): BigInt = bigIntOf(this)
fun UInt.toMutableBigInt(): MutableBigInt = mutableBigIntOf(this)

fun ULong.toBigInt(): BigInt = bigIntOf(this)
fun ULong.toMutableBigInt(): MutableBigInt = mutableBigIntOf(this)

operator fun BigInt.plus(rhs: UByte): BigInt = plus(rhs.toBigInt())
operator fun BigInt.plus(rhs: UShort): BigInt = plus(rhs.toBigInt())
operator fun BigInt.plus(rhs: UInt): BigInt = plus(rhs.toBigInt())
operator fun BigInt.plus(rhs: ULong): BigInt = plus(rhs.toBigInt())

operator fun MutableBigInt.plus(rhs: UByte): MutableBigInt = plus(rhs.toMutableBigInt())
operator fun MutableBigInt.plus(rhs: UShort): MutableBigInt = plus(rhs.toMutableBigInt())
operator fun MutableBigInt.plus(rhs: UInt): MutableBigInt = plus(rhs.toMutableBigInt())
operator fun MutableBigInt.plus(rhs: ULong): MutableBigInt = plus(rhs.toMutableBigInt())

operator fun MutableBigInt.plusAssign(rhs: UByte) = plusAssign(rhs.toMutableBigInt())
operator fun MutableBigInt.plusAssign(rhs: UShort) = plusAssign(rhs.toMutableBigInt())
operator fun MutableBigInt.plusAssign(rhs: UInt) = plusAssign(rhs.toMutableBigInt())
operator fun MutableBigInt.plusAssign(rhs: ULong) = plusAssign(rhs.toMutableBigInt())

operator fun BigInt.minus(rhs: UByte): BigInt = minus(rhs.toBigInt())
operator fun BigInt.minus(rhs: UShort): BigInt = minus(rhs.toBigInt())
operator fun BigInt.minus(rhs: UInt): BigInt = minus(rhs.toBigInt())
operator fun BigInt.minus(rhs: ULong): BigInt = minus(rhs.toBigInt())

operator fun MutableBigInt.minus(rhs: UByte): MutableBigInt = minus(rhs.toMutableBigInt())
operator fun MutableBigInt.minus(rhs: UShort): MutableBigInt = minus(rhs.toMutableBigInt())
operator fun MutableBigInt.minus(rhs: UInt): MutableBigInt = minus(rhs.toMutableBigInt())
operator fun MutableBigInt.minus(rhs: ULong): MutableBigInt = minus(rhs.toMutableBigInt())

operator fun MutableBigInt.minusAssign(rhs: UByte) = minusAssign(rhs.toMutableBigInt())
operator fun MutableBigInt.minusAssign(rhs: UShort) = minusAssign(rhs.toMutableBigInt())
operator fun MutableBigInt.minusAssign(rhs: UInt) = minusAssign(rhs.toMutableBigInt())
operator fun MutableBigInt.minusAssign(rhs: ULong) = minusAssign(rhs.toMutableBigInt())

operator fun BigInt.times(rhs: UByte): BigInt = times(rhs.toBigInt())
operator fun BigInt.times(rhs: UShort): BigInt = times(rhs.toBigInt())
operator fun BigInt.times(rhs: UInt): BigInt = times(rhs.toBigInt())
operator fun BigInt.times(rhs: ULong): BigInt = times(rhs.toBigInt())

operator fun MutableBigInt.times(rhs: UByte): MutableBigInt = times(rhs.toMutableBigInt())
operator fun MutableBigInt.times(rhs: UShort): MutableBigInt = times(rhs.toMutableBigInt())
operator fun MutableBigInt.times(rhs: UInt): MutableBigInt = times(rhs.toMutableBigInt())
operator fun MutableBigInt.times(rhs: ULong): MutableBigInt = times(rhs.toMutableBigInt())

operator fun MutableBigInt.timesAssign(rhs: UByte) = timesAssign(rhs.toMutableBigInt())
operator fun MutableBigInt.timesAssign(rhs: UShort) = timesAssign(rhs.toMutableBigInt())
operator fun MutableBigInt.timesAssign(rhs: UInt) = timesAssign(rhs.toMutableBigInt())
operator fun MutableBigInt.timesAssign(rhs: ULong) = timesAssign(rhs.toMutableBigInt())
//
//operator fun BigInt.div(rhs: UByte): BigInt = div(rhs.toBigInt())
//operator fun BigInt.div(rhs: UShort): BigInt = div(rhs.toBigInt())
//operator fun BigInt.div(rhs: UInt): BigInt = div(rhs.toBigInt())
//operator fun BigInt.div(rhs: ULong): BigInt = div(rhs.toBigInt())
//
//operator fun MutableBigInt.div(rhs: UByte): MutableBigInt = div(rhs.toMutableBigInt())
//operator fun MutableBigInt.div(rhs: UShort): MutableBigInt = div(rhs.toMutableBigInt())
//operator fun MutableBigInt.div(rhs: UInt): MutableBigInt = div(rhs.toMutableBigInt())
//operator fun MutableBigInt.div(rhs: ULong): MutableBigInt = div(rhs.toMutableBigInt())
//
//operator fun MutableBigInt.divAssign(rhs: UByte) = divAssign(rhs.toMutableBigInt())
//operator fun MutableBigInt.divAssign(rhs: UShort) = divAssign(rhs.toMutableBigInt())
//operator fun MutableBigInt.divAssign(rhs: UInt) = divAssign(rhs.toMutableBigInt())
//operator fun MutableBigInt.divAssign(rhs: ULong) = divAssign(rhs.toMutableBigInt())
//
//operator fun BigInt.rem(rhs: UByte): BigInt = rem(rhs.toBigInt())
//operator fun BigInt.rem(rhs: UShort): BigInt = rem(rhs.toBigInt())
//operator fun BigInt.rem(rhs: UInt): BigInt = rem(rhs.toBigInt())
//operator fun BigInt.rem(rhs: ULong): BigInt = rem(rhs.toBigInt())
//
//operator fun MutableBigInt.rem(rhs: UByte): MutableBigInt = rem(rhs.toMutableBigInt())
//operator fun MutableBigInt.rem(rhs: UShort): MutableBigInt = rem(rhs.toMutableBigInt())
//operator fun MutableBigInt.rem(rhs: UInt): MutableBigInt = rem(rhs.toMutableBigInt())
//operator fun MutableBigInt.rem(rhs: ULong): MutableBigInt = rem(rhs.toMutableBigInt())
//
//operator fun MutableBigInt.remAssign(rhs: UByte) = remAssign(rhs.toMutableBigInt())
//operator fun MutableBigInt.remAssign(rhs: UShort) = remAssign(rhs.toMutableBigInt())
//operator fun MutableBigInt.remAssign(rhs: UInt) = remAssign(rhs.toMutableBigInt())
//operator fun MutableBigInt.remAssign(rhs: ULong) = remAssign(rhs.toMutableBigInt())
