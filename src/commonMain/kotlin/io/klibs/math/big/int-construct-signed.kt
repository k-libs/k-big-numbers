package io.klibs.math.big

fun bigIntOf(value: Byte): BigInt = bigIntOf(value.toInt())

fun bigIntOf(value: Short): BigInt = bigIntOf(value.toInt())

fun bigIntOf(value: Int): BigInt =
  when {
    value == -1 -> BigInt.NegativeOne
    value == 0  -> BigInt.Zero
    value == 1  -> BigInt.One
    value == 10 -> BigInt.Ten
    value <  0  -> BigIntImpl(-1, intArrayOf(-value))
    else        -> BigIntImpl(1, intArrayOf(value))
  }

fun bigIntOf(value: Long): BigInt {
  when (value) {
    -1L -> return BigInt.NegativeOne
    0L  -> return BigInt.Zero
    1L  -> return BigInt.One
    10L -> return BigInt.Ten
  }

  val l: Long
  val s: Byte

  if (value < 0) {
    l = -value
    s = -1
  } else {
    l = value
    s = 1
  }

  val high = (l ushr 32).toInt()

  return if (high == 0)
    BigIntImpl(s, intArrayOf(l.toInt()))
  else
    BigIntImpl(s, intArrayOf(high, l.toInt()))
}


fun Byte.toBigInt() = bigIntOf(this)
fun Short.toBigInt() = bigIntOf(this)
fun Int.toBigInt() = bigIntOf(this)
fun Long.toBigInt() = bigIntOf(this)