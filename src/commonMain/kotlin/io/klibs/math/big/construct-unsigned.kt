package io.klibs.math.big

fun bigIntOf(value: UByte): BigInt = bigIntOf(value.toUInt())

fun bigIntOf(value: UShort): BigInt = bigIntOf(value.toUInt())

fun bigIntOf(value: UInt): BigInt =
  when (value) {
    0u   -> BigInt.Zero
    1u   -> BigInt.One
    10u  -> BigInt.Ten
    else -> value.toLong().toBigInt()
  }

fun bigIntOf(value: ULong): BigInt =
  when (value) {
    0uL  -> BigInt.Zero
    1uL  -> BigInt.One
    10uL -> BigInt.Ten
    else -> value.toString().toBigInt()
  }


fun UByte.toBigInt() = bigIntOf(this)
fun UShort.toBigInt() = bigIntOf(this)
fun UInt.toBigInt() = bigIntOf(this)
fun ULong.toBigInt() = bigIntOf(this)