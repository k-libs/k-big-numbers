package io.klibs.math.big

// Operator Functions

inline operator fun BigInt.plus(rhs: Byte): BigInt = plus(rhs.toBigInt())
inline operator fun BigInt.plus(rhs: Short): BigInt = plus(rhs.toBigInt())
inline operator fun BigInt.plus(rhs: Int): BigInt = plus(rhs.toBigInt())
inline operator fun BigInt.plus(rhs: Long): BigInt = plus(rhs.toBigInt())

inline operator fun BigInt.minus(rhs: Byte): BigInt = minus(rhs.toBigInt())
inline operator fun BigInt.minus(rhs: Short): BigInt = minus(rhs.toBigInt())
inline operator fun BigInt.minus(rhs: Int): BigInt = minus(rhs.toBigInt())
inline operator fun BigInt.minus(rhs: Long): BigInt = minus(rhs.toBigInt())

inline operator fun BigInt.times(rhs: Byte): BigInt = times(rhs.toBigInt())
inline operator fun BigInt.times(rhs: Short): BigInt = times(rhs.toBigInt())
inline operator fun BigInt.times(rhs: Int): BigInt = times(rhs.toBigInt())
inline operator fun BigInt.times(rhs: Long): BigInt = times(rhs.toBigInt())

inline operator fun BigInt.div(rhs: Byte): BigInt = div(rhs.toBigInt())
inline operator fun BigInt.div(rhs: Short): BigInt = div(rhs.toBigInt())
inline operator fun BigInt.div(rhs: Int): BigInt = div(rhs.toBigInt())
inline operator fun BigInt.div(rhs: Long): BigInt = div(rhs.toBigInt())

inline operator fun BigInt.rem(rhs: Byte): BigInt = rem(rhs.toBigInt())
inline operator fun BigInt.rem(rhs: Short): BigInt = rem(rhs.toBigInt())
inline operator fun BigInt.rem(rhs: Int): BigInt = rem(rhs.toBigInt())
inline operator fun BigInt.rem(rhs: Long): BigInt = rem(rhs.toBigInt())

// Infix Functions

inline infix fun BigInt.and(rhs: Byte): BigInt = and(rhs.toBigInt())
inline infix fun BigInt.and(rhs: Short): BigInt = and(rhs.toBigInt())
inline infix fun BigInt.and(rhs: Int): BigInt = and(rhs.toBigInt())
inline infix fun BigInt.and(rhs: Long): BigInt = and(rhs.toBigInt())

inline infix fun BigInt.or(rhs: Byte): BigInt = or(rhs.toBigInt())
inline infix fun BigInt.or(rhs: Short): BigInt = or(rhs.toBigInt())
inline infix fun BigInt.or(rhs: Int): BigInt = or(rhs.toBigInt())
inline infix fun BigInt.or(rhs: Long): BigInt = or(rhs.toBigInt())

inline infix fun BigInt.xor(rhs: Byte): BigInt = xor(rhs.toBigInt())
inline infix fun BigInt.xor(rhs: Short): BigInt = xor(rhs.toBigInt())
inline infix fun BigInt.xor(rhs: Int): BigInt = xor(rhs.toBigInt())
inline infix fun BigInt.xor(rhs: Long): BigInt = xor(rhs.toBigInt())

inline infix fun BigInt.andNot(rhs: Byte): BigInt = andNot(rhs.toBigInt())
inline infix fun BigInt.andNot(rhs: Short): BigInt = andNot(rhs.toBigInt())
inline infix fun BigInt.andNot(rhs: Int): BigInt = andNot(rhs.toBigInt())
inline infix fun BigInt.andNot(rhs: Long): BigInt = andNot(rhs.toBigInt())

