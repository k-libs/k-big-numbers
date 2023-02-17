package io.klibs.math.big

// Operator Functions

inline operator fun BigInt.plus(rhs: UByte): BigInt = plus(rhs.toBigInt())
inline operator fun BigInt.plus(rhs: UShort): BigInt = plus(rhs.toBigInt())
inline operator fun BigInt.plus(rhs: UInt): BigInt = plus(rhs.toBigInt())
inline operator fun BigInt.plus(rhs: ULong): BigInt = plus(rhs.toBigInt())

inline operator fun BigInt.minus(rhs: UByte): BigInt = minus(rhs.toBigInt())
inline operator fun BigInt.minus(rhs: UShort): BigInt = minus(rhs.toBigInt())
inline operator fun BigInt.minus(rhs: UInt): BigInt = minus(rhs.toBigInt())
inline operator fun BigInt.minus(rhs: ULong): BigInt = minus(rhs.toBigInt())

inline operator fun BigInt.times(rhs: UByte): BigInt = times(rhs.toBigInt())
inline operator fun BigInt.times(rhs: UShort): BigInt = times(rhs.toBigInt())
inline operator fun BigInt.times(rhs: UInt): BigInt = times(rhs.toBigInt())
inline operator fun BigInt.times(rhs: ULong): BigInt = times(rhs.toBigInt())

inline operator fun BigInt.div(rhs: UByte): BigInt = div(rhs.toBigInt())
inline operator fun BigInt.div(rhs: UShort): BigInt = div(rhs.toBigInt())
inline operator fun BigInt.div(rhs: UInt): BigInt = div(rhs.toBigInt())
inline operator fun BigInt.div(rhs: ULong): BigInt = div(rhs.toBigInt())

inline operator fun BigInt.rem(rhs: UByte): BigInt = rem(rhs.toBigInt())
inline operator fun BigInt.rem(rhs: UShort): BigInt = rem(rhs.toBigInt())
inline operator fun BigInt.rem(rhs: UInt): BigInt = rem(rhs.toBigInt())
inline operator fun BigInt.rem(rhs: ULong): BigInt = rem(rhs.toBigInt())

// Infix Functions

inline infix fun BigInt.and(rhs: UByte): BigInt = and(rhs.toBigInt())
inline infix fun BigInt.and(rhs: UShort): BigInt = and(rhs.toBigInt())
inline infix fun BigInt.and(rhs: UInt): BigInt = and(rhs.toBigInt())
inline infix fun BigInt.and(rhs: ULong): BigInt = and(rhs.toBigInt())

inline infix fun BigInt.or(rhs: UByte): BigInt = or(rhs.toBigInt())
inline infix fun BigInt.or(rhs: UShort): BigInt = or(rhs.toBigInt())
inline infix fun BigInt.or(rhs: UInt): BigInt = or(rhs.toBigInt())
inline infix fun BigInt.or(rhs: ULong): BigInt = or(rhs.toBigInt())

inline infix fun BigInt.xor(rhs: UByte): BigInt = xor(rhs.toBigInt())
inline infix fun BigInt.xor(rhs: UShort): BigInt = xor(rhs.toBigInt())
inline infix fun BigInt.xor(rhs: UInt): BigInt = xor(rhs.toBigInt())
inline infix fun BigInt.xor(rhs: ULong): BigInt = xor(rhs.toBigInt())

inline infix fun BigInt.andNot(rhs: UByte): BigInt = andNot(rhs.toBigInt())
inline infix fun BigInt.andNot(rhs: UShort): BigInt = andNot(rhs.toBigInt())
inline infix fun BigInt.andNot(rhs: UInt): BigInt = andNot(rhs.toBigInt())
inline infix fun BigInt.andNot(rhs: ULong): BigInt = andNot(rhs.toBigInt())

