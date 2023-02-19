package io.klibs.math.big

internal val INFLATED_BIGINT = INFLATED.toBigInt()

internal val LONG_TEN_POWERS_TABLE = longArrayOf(
  1,                   // 0 / 10^0
  10,                  // 1 / 10^1
  100,                 // 2 / 10^2
  1000,                // 3 / 10^3
  10000,               // 4 / 10^4
  100000,              // 5 / 10^5
  1000000,             // 6 / 10^6
  10000000,            // 7 / 10^7
  100000000,           // 8 / 10^8
  1000000000,          // 9 / 10^9
  10000000000L,        // 10 / 10^10
  100000000000L,       // 11 / 10^11
  1000000000000L,      // 12 / 10^12
  10000000000000L,     // 13 / 10^13
  100000000000000L,    // 14 / 10^14
  1000000000000000L,   // 15 / 10^15
  10000000000000000L,  // 16 / 10^16
  100000000000000000L, // 17 / 10^17
  1000000000000000000L // 18 / 10^18
)

internal var BIG_TEN_POWERS_TABLE: Array<BigInt> = arrayOf(
  BigInt.One,
  BigInt.Ten,
  100.toBigInt(),
  1000.toBigInt(),
  10000.toBigInt(),
  100000.toBigInt(),
  1000000.toBigInt(),
  10000000.toBigInt(),
  100000000.toBigInt(),
  1000000000.toBigInt(),
  10000000000L.toBigInt(),
  100000000000L.toBigInt(),
  1000000000000L.toBigInt(),
  10000000000000L.toBigInt(),
  100000000000000L.toBigInt(),
  1000000000000000L.toBigInt(),
  10000000000000000L.toBigInt(),
  100000000000000000L.toBigInt(),
  1000000000000000000L.toBigInt()
)

internal val ZERO_SCALED_BY = arrayOf(
  BigDec.Zero,
  BigDecImpl(BigInt.Zero, 0, 1, 1),
  BigDecImpl(BigInt.Zero, 0, 2, 1),
  BigDecImpl(BigInt.Zero, 0, 3, 1),
  BigDecImpl(BigInt.Zero, 0, 4, 1),
  BigDecImpl(BigInt.Zero, 0, 5, 1),
  BigDecImpl(BigInt.Zero, 0, 6, 1),
  BigDecImpl(BigInt.Zero, 0, 7, 1),
  BigDecImpl(BigInt.Zero, 0, 8, 1),
  BigDecImpl(BigInt.Zero, 0, 9, 1),
  BigDecImpl(BigInt.Zero, 0, 10, 1),
  BigDecImpl(BigInt.Zero, 0, 11, 1),
  BigDecImpl(BigInt.Zero, 0, 12, 1),
  BigDecImpl(BigInt.Zero, 0, 13, 1),
  BigDecImpl(BigInt.Zero, 0, 14, 1),
  BigDecImpl(BigInt.Zero, 0, 15, 1)
)

internal const val HALF_LONG_MAX_VALUE = Long.MAX_VALUE / 2
internal const val HALF_LONG_MIN_VALUE = Long.MIN_VALUE / 2

internal val BIG_TEN_POWERS_TABLE_INITLEN: Int = BIG_TEN_POWERS_TABLE.size
internal val BIG_TEN_POWERS_TABLE_MAX = 16 * BIG_TEN_POWERS_TABLE_INITLEN

internal fun parseExp(value: CharArray, offset: Int, len: Int): Long {
  var offset = offset
  var len = len
  var exp: Long = 0

  offset++

  var c = value[offset]

  len--

  val negexp = c == '-'

  if (negexp || c == '+') {
    offset++
    c = value[offset]
    len--
  }

  if (len <= 0)
    throw NumberFormatException("no exponent digits")

  while (len > 10 && (c == '0' || (c.digitToIntOrNull() ?: -1) == 0)) {
    offset++
    c = value[offset]
    len--
  }

  if (len > 10)
    throw NumberFormatException("too many nonzero exponent digits")

  while (true) {
    val v = if (c >= '0' && c <= '9') {
      c.code - '0'.code
    } else {
      c.digitToIntOrNull() ?: throw NumberFormatException("not a digit.")
    }

    exp = exp * 10 + v

    if (len == 1)
      break

    offset++
    c = value[offset]
    len--
  }

  if (negexp) // apply sign
    exp = -exp

  return exp
}


internal fun adjustScale(scl: Int, exp: Long): Int {
  var scl = scl
  val adjustedScale = scl - exp

  if (adjustedScale > Int.MAX_VALUE || adjustedScale < Int.MIN_VALUE)
    throw NumberFormatException("scale out of range.")

  scl = adjustedScale.toInt()

  return scl
}

internal fun checkScaleNonZero(value: Long): Int {
  val asInt = value.toInt()

  if (asInt.toLong() != value) {
    throw ArithmeticException(if (asInt > 0) "underflow" else "overflow")
  }

  return asInt
}

internal fun compactValFor(b: BigInt): Long {
  val m: IntArray = (b as BigIntImpl).mag
  val len = m.size

  if (len == 0)
    return 0

  val d = m[0]
  if (len > 2 || len == 2 && d < 0)
    return INFLATED

  val u = if (len == 2)
    (m[1].toLong() and LONG_MASK) + (d.toLong() shl 32)
  else
    d.toLong() and LONG_MASK

  return if (b.signum < 0) -u else u
}

internal fun longDigitLength(x: Long): Int {
  var x = x

  if (x < 0)
    x = -x

  if (x < 10)
    return 1

  val r = (64 - x.countLeadingZeroBits() + 1) * 1233 ushr 12
  val tab = LONG_TEN_POWERS_TABLE

  return if (r >= tab.size || x < tab[r]) r else r + 1
}

internal fun divideAndRoundByTenPow(
  intVal: BigInt?,
  tenPow: Int,
  roundingMode: RoundingMode,
): BigInt {
  return if (tenPow < LONG_TEN_POWERS_TABLE.size)
    divideAndRound(intVal as BigIntImpl, LONG_TEN_POWERS_TABLE[tenPow], roundingMode)
  else
    divideAndRound(intVal as BigIntImpl, bigTenToThe(tenPow) as BigIntImpl, roundingMode)
}

internal fun divideAndRound(
  ldividend: Long,
  ldivisor: Long,
  roundingMode: RoundingMode,
): Long {
  val qsign: Int
  val q = ldividend / ldivisor

  if (roundingMode == RoundingMode.Down)
    return q

  val r = ldividend % ldivisor
  qsign = if (ldividend < 0 == ldivisor < 0) 1 else -1

  return if (r != 0L) {
    val increment: Boolean = needIncrement(ldivisor, roundingMode, qsign, q, r)

    if (increment)
      q + qsign
    else
      q
  } else {
    q
  }
}

internal fun divideAndRound(bdividend: BigIntImpl, bdivisor: BigIntImpl, roundingMode: RoundingMode): BigIntImpl {
  val isRemainderZero: Boolean // record remainder is zero or not
  val qsign: Int // quotient sign
  // Descend into mutables for faster remainder checks
  val mdividend = MutableBigIntImpl(bdividend.mag)
  val mq = MutableBigIntImpl()
  val mdivisor = MutableBigIntImpl(bdivisor.mag)
  val mr: MutableBigIntImpl = mdividend.divide(mdivisor, mq)
  isRemainderZero = mr.isZero()
  qsign = if (bdividend.signum != bdivisor.signum) -1 else 1
  if (!isRemainderZero) {
    if (needIncrement(mdivisor, roundingMode, qsign, mq, mr)) {
      mq.add(MutableBigIntImpl(1))
    }
  }
  return mq.toBigInt(qsign)
}


internal fun commonNeedIncrement(
  roundingMode: RoundingMode,
  qsign: Int,
  cmpFracHalf: Int,
  oddQuot: Boolean,
): Boolean {
  return when (roundingMode) {
    RoundingMode.Unnecessary -> throw ArithmeticException("rounding necessary")
    RoundingMode.Up          -> true
    RoundingMode.Down        -> false
    RoundingMode.Ceiling     -> qsign > 0
    RoundingMode.Floor       -> qsign < 0
    else                     -> {
      if (cmpFracHalf < 0)
        false
      else if (cmpFracHalf > 0)
        true
      else { // half-way
        when (roundingMode) {
          RoundingMode.HalfDown -> false
          RoundingMode.HalfUp   -> true
          RoundingMode.HalfEven -> oddQuot
          else                  -> throw AssertionError("unexpected rounding mode $roundingMode")
        }
      }
    }
  }
}

internal fun needIncrement(
  ldivisor: Long,
  roundingMode: RoundingMode,
  qsign: Int,
  q: Long,
  r: Long,
): Boolean {
  val cmpFracHalf: Int

  cmpFracHalf = if (r <= HALF_LONG_MIN_VALUE || r > HALF_LONG_MAX_VALUE) {
    1 // 2 * r can't fit into long
  } else {
    longCompareMagnitude(2 * r, ldivisor)
  }

  return commonNeedIncrement(roundingMode, qsign, cmpFracHalf, q and 1L != 0L)
}

internal fun longCompareMagnitude(x: Long, y: Long): Int {
  var x = x
  var y = y

  if (x < 0)
    x = -x

  if (y < 0)
    y = -y

  return if (x < y) -1 else if (x == y) 0 else 1
}

internal fun bigDigitLength(b: BigInt): Int {
  if ((b as BigIntImpl).signum == BYTE_ZERO)
    return 1

  val r: Int = ((b.bitLength().toLong() + 1) * 646456993 ushr 31).toInt()

  return if (b.compareMagnitude(bigTenToThe(r) as BigIntImpl) < 0) r else r + 1
}

internal fun bigTenToThe(n: Int): BigInt {
  if (n < 0)
    return BigInt.Zero

  if (n < BIG_TEN_POWERS_TABLE_MAX) {
    val pows: Array<BigInt> = BIG_TEN_POWERS_TABLE

    return if (n < pows.size)
      pows[n]
    else
      expandBigIntegerTenPowers(n)
  }

  return BigInt.Ten.pow(n)
}

@Suppress("UNCHECKED_CAST")
internal fun expandBigIntegerTenPowers(n: Int): BigInt {
  var pows: Array<BigInt?> = BIG_TEN_POWERS_TABLE as Array<BigInt?>
  val curLen = pows.size

  if (curLen <= n) {
    var newLen = curLen shl 1

    while (newLen <= n) {
      newLen = newLen shl 1
    }

    pows = pows.copyOf(newLen)

    for (i in curLen until newLen) {
      pows[i] = pows[i - 1]!!.times(BigInt.Ten)
    }

    BIG_TEN_POWERS_TABLE = pows as Array<BigInt>
  }

  return pows[n]!!
}

internal fun zeroValueOf(scale: Int): BigDec? {
  return if (scale >= 0 && scale < ZERO_SCALED_BY.size)
    ZERO_SCALED_BY[scale]
  else
    BigDecImpl(BigInt.Zero, 0, scale, 1)
}

internal fun createAndStripZerosToMatchScale(compactVal: Long, scale: Int, preferredScale: Long): BigDec {
  var compactVal = compactVal
  var scale = scale

  while (abs(compactVal) >= 10L && scale > preferredScale) {
    if (compactVal and 1L != 0L)
      break

    val r = compactVal % 10L
    if (r != 0L)
      break

    compactVal /= 10
    scale = checkScale(compactVal, scale.toLong() - 1)
  }

  return bigDecimalOf(compactVal, scale)
}

internal fun checkScale(intCompact: Long, value: Long): Int {
  var asInt = value.toInt()

  if (asInt.toLong() != value) {
    asInt = if (value > Int.MAX_VALUE) Int.MAX_VALUE else Int.MIN_VALUE

    if (intCompact != 0L)
      throw ArithmeticException(if (asInt > 0) "Underflow" else "Overflow")
  }

  return asInt
}

internal fun divideAndRound(bdividend: BigIntImpl, ldivisor: Long, roundingMode: RoundingMode): BigIntImpl {
  // Descend into mutables for faster remainder checks
  val mdividend = MutableBigIntImpl(bdividend.mag)
  // store quotient
  val mq = MutableBigIntImpl()
  // store quotient & remainder in long
  val r: Long = mdividend.divide(ldivisor, mq)
  // record remainder is zero or not
  val isRemainderZero = r == 0L
  // quotient sign
  val qsign = if (ldivisor < 0) -bdividend.signum else bdividend.signum.toInt()

  if (!isRemainderZero) {
    if (needIncrement(ldivisor, roundingMode, qsign, mq, r)) {
      mq.add(MutableBigIntImpl(1))
    }
  }
  return mq.toBigInt(qsign)
}

internal fun needIncrement(
  mdivisor: MutableBigIntImpl,
  roundingMode: RoundingMode,
  qsign: Int,
  mq: MutableBigIntImpl,
  mr: MutableBigIntImpl,
): Boolean {
  val cmpFracHalf: Int = mr.compareHalf(mdivisor)
  return commonNeedIncrement(roundingMode, qsign, cmpFracHalf, mq.isOdd())
}

internal fun needIncrement(
  ldivisor: Long,
  roundingMode: RoundingMode,
  qsign: Int,
  mq: MutableBigIntImpl,
  r: Long,
): Boolean {
  val cmpFracHalf: Int

  cmpFracHalf = if (r <= HALF_LONG_MIN_VALUE || r > HALF_LONG_MAX_VALUE) {
    1
  } else {
    longCompareMagnitude(2 * r, ldivisor)
  }

  return commonNeedIncrement(roundingMode, qsign, cmpFracHalf, mq.isOdd())
}
