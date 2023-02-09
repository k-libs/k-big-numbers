package io.klibs.math

inline fun Byte.toBigInt() = bigIntOf(this)
inline fun Short.toBigInt() = bigIntOf(this)
inline fun Int.toBigInt() = bigIntOf(this)
inline fun Long.toBigInt() = bigIntOf(this)
inline fun UByte.toBigInt() = bigIntOf(this)
inline fun UShort.toBigInt() = bigIntOf(this)
inline fun UInt.toBigInt() = bigIntOf(this)
inline fun ULong.toBigInt() = bigIntOf(this)

operator fun Byte.plus(lhs: BigInt) = lhs + this
operator fun Short.plus(lhs: BigInt) = lhs + this
operator fun Int.plus(lhs: BigInt) = lhs + this
operator fun Long.plus(lhs: BigInt) = lhs + this
operator fun UByte.plus(lhs: BigInt) = lhs + this
operator fun UShort.plus(lhs: BigInt) = lhs + this
operator fun UInt.plus(lhs: BigInt) = lhs + this
operator fun ULong.plus(lhs: BigInt) = lhs + this

operator fun Byte.minus(lhs: BigInt) = toBigInt() - lhs
operator fun Short.minus(lhs: BigInt) = toBigInt() - lhs
operator fun Int.minus(lhs: BigInt) = toBigInt() - lhs
operator fun Long.minus(lhs: BigInt) = toBigInt() - lhs
operator fun UByte.minus(lhs: BigInt) = toBigInt() - lhs
operator fun UShort.minus(lhs: BigInt) = toBigInt() - lhs
operator fun UInt.minus(lhs: BigInt) = toBigInt() - lhs
operator fun ULong.minus(lhs: BigInt) = toBigInt() - lhs

operator fun Byte.times(lhs: BigInt) = lhs * this
operator fun Short.times(lhs: BigInt) = lhs * this
operator fun Int.times(lhs: BigInt) = lhs * this
operator fun Long.times(lhs: BigInt) = lhs * this
operator fun UByte.times(lhs: BigInt) = lhs * this
operator fun UShort.times(lhs: BigInt) = lhs * this
operator fun UInt.times(lhs: BigInt) = lhs * this
operator fun ULong.times(lhs: BigInt) = lhs * this

operator fun Byte.div(lhs: BigInt) = toBigInt() / lhs
operator fun Short.div(lhs: BigInt) = toBigInt() / lhs
operator fun Int.div(lhs: BigInt) = toBigInt() / lhs
operator fun Long.div(lhs: BigInt) = toBigInt() / lhs
operator fun UByte.div(lhs: BigInt) = toBigInt() / lhs
operator fun UShort.div(lhs: BigInt) = toBigInt() / lhs
operator fun UInt.div(lhs: BigInt) = toBigInt() / lhs
operator fun ULong.div(lhs: BigInt) = toBigInt() / lhs

operator fun Byte.rem(lhs: BigInt) = toBigInt() % lhs
operator fun Short.rem(lhs: BigInt) = toBigInt() % lhs
operator fun Int.rem(lhs: BigInt) = toBigInt() % lhs
operator fun Long.rem(lhs: BigInt) = toBigInt() % lhs
operator fun UByte.rem(lhs: BigInt) = toBigInt() % lhs
operator fun UShort.rem(lhs: BigInt) = toBigInt() % lhs
operator fun UInt.rem(lhs: BigInt) = toBigInt() % lhs
operator fun ULong.rem(lhs: BigInt) = toBigInt() % lhs