package io.klibs.math.big

import kotlin.math.*

private const val MULTIPLY_SQUARE_THRESHOLD = 20

private const val KARATSUBA_THRESHOLD = 80
private const val KARATSUBA_SQUARE_THRESHOLD = 128

private const val TOOM_COOK_THRESHOLD = 240
private const val TOOM_COOK_SQUARE_THRESHOLD = 216

private const val SCHOENHAGE_BASE_CONVERSION_THRESHOLD = 20

private const val ZEROES = "0000000000000000000000000000000000000000000000000000000000000000"

private val LOG_TWO = log2(2.0)

private var powerCache = initPowerCache()
private val logCache by lazy { initLogCache() }
private val digitsPerLong by lazy { initDigitsPerLong() }
private val longRadix by lazy { initLongRadixCache() }

internal class BigIntImpl : BigInt {

  private val signum: Byte
  internal val mag: IntArray

  override val isZero: Boolean
    get() = signum == BYTE_ZERO

  override val isNegative: Boolean
    get() = signum < BYTE_ZERO

  override val isPositive: Boolean
    get() = signum > BYTE_ZERO

  constructor(signum: Byte, mag: IntArray) {
    this.signum = signum
    this.mag = mag
  }

  private constructor(value: Long) {
    val v = if (value < 0) -value else value

    signum = if (value < 0) -1 else 1

    val highWord = (v ushr 32).toInt()
    if (highWord == 0) {
      mag = IntArray(1)
      mag[0] = v.toInt()
    } else {
      mag = IntArray(2)
      mag[0] = highWord
      mag[1] = v.toInt()
    }
  }

  private constructor(value: IntArray) {
    if (value.isEmpty())
      throw NumberFormatException("zero length BigInt")

    if (value[0] < 0) {
      mag = makePositive(value)
      signum = -1
    } else {
      mag = trustedStripLeadingZeroInts(value)
      signum = if (mag.isEmpty()) 0 else 1
    }

    checkRange(mag)
  }

  override fun fitsByte(): Boolean =
    if (signum == BYTE_ZERO)
      true
    else if (mag.size == 1)
      bitLength() <= 7
    else
      false

  override fun fitsShort(): Boolean =
    if (signum == BYTE_ZERO)
      true
    else if (mag.size == 1)
      bitLength() <= 15
    else
      false

  override fun fitsInt() =
    if (signum == BYTE_ZERO) {
      true
    } else if (mag.size == 1) {
      bitLength() <= 31
    } else {
      false
    }

  override fun fitsLong() =
    if (signum == BYTE_ZERO)
      true
    else if (mag.size == 2)
      bitLength() <= 63
    else
      mag.size == 1

  override fun fitsUByte() =
    when {
      signum < 0 -> false
      signum > 0 -> bitLength() <= 8
      else       -> true
    }

  override fun fitsUShort() =
    when {
      signum < 0 -> false
      signum > 0 -> bitLength() <= 16
      else       -> true
    }

  override fun fitsUInt() =
    when {
      signum < 0 -> false
      signum > 0 -> bitLength() <= 32
      else       -> true
    }

  override fun fitsULong() =
    when {
      signum < 0 -> false
      signum > 0 -> bitLength() <= 64
      else       -> true
    }

  override fun toByte() =
    if (fitsByte())
      when {
        signum < 0 -> (-mag[0]).toByte()
        signum > 0 -> mag[0].toByte()
        else       -> 0.toByte()
      }
    else
      throw NumberCastException("cannot cast value $this as a Byte")

  override fun toShort() =
    if (fitsShort())
      when {
        signum < 0 -> (-mag[0]).toShort()
        signum > 0 -> mag[0].toShort()
        else       -> 0.toShort()
      }
    else
      throw NumberCastException("cannot cast value $this as a Short")

  override fun toInt() =
    if (fitsInt())
      when {
        signum < 0 -> if (mag[0] > 0) -mag[0] else mag[0]
        signum > 0 -> mag[0]
        else       -> 0
      }
    else
      throw NumberCastException("cannot cast value $this as an Int")

  override fun toLong() =
    if (fitsLong())
      longValue()
    else
      throw NumberCastException("cannot cast value $this as a Long")

  override fun toUByte() =
    if (fitsUByte())
      when (signum) {
        BYTE_ZERO -> 0.toUByte()
        else      -> mag[0].toUByte()
      }
    else
      throw NumberCastException("cannot cast value $this as a UByte")

  override fun toUShort() =
    if (fitsUShort())
      when (signum) {
        BYTE_ZERO -> 0.toUShort()
        else      -> mag[0].toUShort()
      }
    else
      throw NumberCastException("cannot cast value $this as a UShort")

  override fun toUInt() =
    if (fitsUInt())
      when (mag.size) {
        1    -> mag[0].toUInt()
        else -> 0u
      }
    else
      throw NumberCastException("cannot cast value $this as a UInt")

  override fun toULong() =
    if (fitsULong())
      when (mag.size) {
        1    -> mag[0].toULong()
        2    -> longValue().toULong()
        else -> 0uL
      }
    else
      throw NumberCastException("cannot cast value $this as a ULong")

  override fun plus(rhs: BigInt): BigInt {
    if (rhs.isZero)
      return this
    if (isZero)
      return rhs
    if (isNegative == rhs.isNegative)
      return BigIntImpl(signum, add(mag, (rhs as BigIntImpl).mag))

    val cmp = compareMagnitude(rhs)
    if (cmp == BYTE_ZERO)
      return BigInt.Zero

    val resultMag = trustedStripLeadingZeroInts(
      if (cmp > 0)
        subtract(mag, (rhs as BigIntImpl).mag)
      else
        subtract((rhs as BigIntImpl).mag, mag)
    )

    return BigIntImpl(if (cmp == signum) 1 else -1, resultMag)
  }

  override fun minus(rhs: BigInt): BigInt {
    if (rhs.isZero)
      return this
    if (isZero)
      return -rhs
    if (isNegative != rhs.isNegative)
      return BigIntImpl(signum, add(mag, (rhs as BigIntImpl).mag))

    val cmp = compareMagnitude(rhs)
    if (cmp == BYTE_ZERO)
      return BigInt.Zero

    val result = trustedStripLeadingZeroInts(
      if (cmp > 0)
        subtract(mag, (rhs as BigIntImpl).mag)
      else
        subtract((rhs as BigIntImpl).mag, mag)
    )

    return BigIntImpl(if (cmp == signum) 1 else -1, result)
  }

  override fun unaryMinus() = if (isZero) BigInt.Zero else if (signum > 0) BigIntImpl(-1, mag) else BigIntImpl(1, mag)

  override fun abs() = if (isZero) BigInt.Zero else if (signum > 0) this else -this

  override fun times(rhs: BigInt): BigInt = times(rhs, false)

  override fun div(rhs: BigInt): BigInt {
    rhs as BigIntImpl
    return if (rhs.mag.size < BURNIKEL_ZIEGLER_THRESHOLD || mag.size - rhs.mag.size < BURNIKEL_ZIEGLER_OFFSET) {
      divideKnuth(rhs)
    } else {
      divideBurnikelZiegler(rhs)
    }
  }

  override fun rem(rhs: BigInt): BigInt {
    rhs as BigIntImpl
    return if (rhs.mag.size < BURNIKEL_ZIEGLER_THRESHOLD || mag.size - rhs.mag.size < BURNIKEL_ZIEGLER_OFFSET) {
      remainderKnuth(rhs)
    } else {
      remainderBurnikelZiegler(rhs)
    }
  }

  override fun divideAndRemainder(value: BigInt): Pair<BigInt, BigInt> {
    value as BigIntImpl
    return if (value.mag.size < BURNIKEL_ZIEGLER_THRESHOLD || mag.size - value.mag.size < BURNIKEL_ZIEGLER_OFFSET) {
      divideAndRemainderKnuth(value)
    } else {
      divideAndRemainderBurnikelZiegler(value)
    }
  }

  override fun square(): BigInt = square(false)

  override fun shl(n: Int): BigInt {
    return if (signum == BYTE_ZERO)
      BigInt.Zero
    else if (n > 0)
      BigIntImpl(signum, shiftLeft(mag, n))
    else if (n == 0)
      this
    else {
      shiftRightImpl(-n)
    }
  }

  override fun shr(n: Int): BigInt {
    return if (signum == BYTE_ZERO)
      BigInt.Zero
    else if (n > 0)
      shiftRightImpl(n)
    else if (n == 0)
      this
    else
      BigIntImpl(signum, shiftLeft(mag, -n))
  }

  override fun and(value: BigInt): BigInt {
    val result = IntArray(max(intLength(), (value as BigIntImpl).intLength()))
    for (i in 0 .. result.lastIndex)
      result[i] = getInt(result.lastIndex - i) and value.getInt(result.lastIndex - i)
    return valueOf(result)
  }

  override fun or(value: BigInt): BigInt {
    val result = IntArray(max(intLength(), (value as BigIntImpl).intLength()))
    for (i in 0 .. result.lastIndex)
      result[i] = getInt(result.lastIndex - i) or value.getInt(result.lastIndex - i)
    return valueOf(result)
  }

  override fun xor(value: BigInt): BigInt {
    val result = IntArray(max(intLength(), (value as BigIntImpl).intLength()))
    for (i in 0 .. result.lastIndex)
      result[i] = getInt(result.lastIndex - i) xor value.getInt(result.lastIndex - i)
    return valueOf(result)
  }

  override fun inv(): BigInt {
    val result = IntArray(intLength())
    for (i in 0 .. result.lastIndex)
      result[i] = getInt(result.lastIndex - i).inv()
    return valueOf(result)
  }

  override fun andNot(value: BigInt): BigInt {
    val result = IntArray(max(intLength(), (value as BigIntImpl).intLength()))
    for (i in 0 .. result.lastIndex)
      result[i] = getInt(result.lastIndex - i) and getInt(result.lastIndex - i).inv()
    return valueOf(result)
  }

  override fun pow(exponent: Int): BigInt {
    if (exponent < 0)
      throw ArithmeticException("negative exponent")
    if (signum == BYTE_ZERO)
      return if (exponent == 0) BigInt.One else BigInt.Zero

    var partToSquare = abs() as BigIntImpl

    val powersOfTwo = partToSquare.getLowestSetBit()
    val bitsToShiftLong = powersOfTwo.toLong() * exponent

    if (bitsToShiftLong > Int.MAX_VALUE)
      reportOverflow()

    val bitsToShift = bitsToShiftLong.toInt()

    val remainingBits: Int

    // Factor the powers of two out quickly by shifting right, if needed.

    // Factor the powers of two out quickly by shifting right, if needed.
    if (powersOfTwo > 0) {
      partToSquare = partToSquare.shr(powersOfTwo) as BigIntImpl
      remainingBits = partToSquare.bitLength()
      if (remainingBits == 1) {  // Nothing left but +/- 1?
        return if (signum < 0 && exponent and 1 == 1) {
          BigInt.NegativeOne shl bitsToShift
        } else {
          BigInt.One shl bitsToShift
        }
      }
    } else {
      remainingBits = partToSquare.bitLength()

      if (remainingBits == 1)
        return if (signum < 0 && exponent and 1 == 1) BigInt.NegativeOne else BigInt.One

    }

    val scaleFactor = remainingBits.toLong() * exponent

    if (partToSquare.mag.size == 1 && scaleFactor <= 62) {
      val newSign = if (signum < 0 && exponent and 1 == 1) -1 else 1
      var result = 1L
      var baseToPow2 = partToSquare.mag[0].toLong() and LONG_MASK
      var workingExponent = exponent

      while (workingExponent != 0) {
        if (workingExponent and 1 == 1)
          result *= baseToPow2

        if (1.let { workingExponent = workingExponent ushr it; workingExponent } != 0)
          baseToPow2 *= baseToPow2
      }

      return if (powersOfTwo > 0) {
        if (bitsToShift + scaleFactor <= 62) { // Fits in long?
          valueOf((result shl bitsToShift) * newSign)
        } else {
          valueOf(result * newSign).shl(bitsToShift)
        }
      } else {
        valueOf(result * newSign)
      }
    } else {
      if (bitLength().toLong() * exponent / 32 > MAX_MAG_LENGTH)
        reportOverflow()

      var answer = BigInt.One as BigIntImpl
      var workingExponent = exponent
      // Perform exponentiation using repeated squaring trick
      while (workingExponent != 0) {
        if (workingExponent and 1 == 1) {
          answer = answer.times(partToSquare) as BigIntImpl
        }
        if (1.let { workingExponent = workingExponent ushr it; workingExponent } != 0) {
          partToSquare = partToSquare.square() as BigIntImpl
        }
      }
      // Multiply back the (exponentiated) powers of two (quickly,
      // by shifting left)
      if (powersOfTwo > 0)
        answer = answer.shl(bitsToShift) as BigIntImpl


      return if (signum < 0 && exponent and 1 == 1) -answer else answer
    }
  }

  override fun getLowestSetBit(): Int {
    var lsb = 0

    if (signum == BYTE_ZERO) {
      lsb -= 1
    } else {
      var i = 0
      var b: Int

      while (getInt(i).also { b = it } == 0)
        i++

      lsb += (i shl 5) + b.countTrailingZeroBits()
    }

    return lsb
  }

  override fun bitLength(): Int {
    val n: Int
    val m   = mag
    val len = m.size

    if (len == 0) {
      n = 0
    } else {
      val magBitLength = ((len - 1) shl 5) + bitLengthForInt(mag[0])

      if (signum < 0) {
        var pow2 = mag[0].countOneBits() == 1
        var i = 1

        while (i < len && pow2) {
          pow2 = mag[i] == 0
          i++
        }

        n = if (pow2) magBitLength - 1 else magBitLength
      } else {
        n = magBitLength
      }
    }

    return n
  }

  override fun bitCount(): Int {
    var bc = 0

    for (i in 0 .. mag.lastIndex)
      bc += mag[i].countOneBits()

    if (signum < 0) {
      var magTrailingZeroCount = 0
      var j = mag.lastIndex

      while (mag[j] == 0) {
        magTrailingZeroCount += 32
        j--
      }

      magTrailingZeroCount = mag[j].countTrailingZeroBits()
      bc += magTrailingZeroCount - 1
    }

    return bc
  }

  override fun toString(radix: Int): String {
    if (signum == BYTE_ZERO)
      return "0"

    val r = if (radix < 2 || radix > 36) 10 else radix

    val abs = abs()
    val b   = abs.bitLength()
    val numChars = floor(LOG_TWO * b / logCache[r]).toInt() + 1 + (if (signum < 0) 1 else 0)
    val sb = StringBuilder(numChars)

    if (signum < 0)
      sb.append('-')

    toString(abs as BigIntImpl, sb, r, 0)

    return sb.toString()
  }

  override fun toString() = toString(10)

  private fun toString(u: BigIntImpl, sb: StringBuilder, radix: Int, digits: Int) {
    if (u.mag.size <= SCHOENHAGE_BASE_CONVERSION_THRESHOLD) {
      u.smallToString(radix, sb, digits)
      return
    }

    val b = u.bitLength()
    val n = round(log(LOG_TWO * b / logCache[radix], E) / LOG_TWO - 1.0).toInt()

    val v = getRadixConversionCache(radix, n)
    val results = u.divideAndRemainder(v)

    val expectedDigits = 1 shl n

    toString(results.first as BigIntImpl, sb, radix, digits - expectedDigits)
    toString(results.second as BigIntImpl, sb, radix, expectedDigits)
  }

  private fun smallToString(radix: Int, buf: StringBuilder, digits: Int) {
    if (signum == BYTE_ZERO) {
      padWithZeros(buf, digits)
      return
    }

    val maxNumDigitGroups = (4 * mag.size + 6) / 7
    val digitGroups = LongArray(maxNumDigitGroups)

    var tmp = this
    var numGroups = 0
    while (tmp.signum != BYTE_ZERO) {
      val d = longRadix[radix]
      val q = MutableBigIntImpl()
      val a = MutableBigIntImpl(tmp.mag)
      val b = MutableBigIntImpl(d.mag)
      val r = a.divide(b, q)
      val q2 = q.toBigInt(tmp.signum * d.signum)
      val r2 = r.toBigInt(tmp.signum * d.signum)
      digitGroups[numGroups++] = r2.longValue()
      tmp = q2
    }

    var s: String = digitGroups[numGroups - 1].toString(radix)

    padWithZeros(buf, digits - (s.length + (numGroups - 1) * digitsPerLong[radix]))
    if (s.startsWith('-'))
      buf.append(s, 1, s.length)
    else
      buf.append(s)

    for (i in numGroups - 2 downTo 0) {

      s = digitGroups[i].toString(radix)
      val numLeadingZeros: Int = digitsPerLong[radix] - s.length
      if (numLeadingZeros != 0) {
        buf.append(ZEROES, 0, numLeadingZeros)
      }
      if (s.startsWith('-'))
        buf.append(s, 1, s.length)
      else
        buf.append(s)
    }
  }

  private fun longValue(): Long {
    var result = 0L

    for (i in 1 downTo 0)
      result = (result shl 32) + (getInt(i).toLong() and LONG_MASK)

    return result
  }

  private fun divideKnuth(value: BigIntImpl): BigIntImpl {
    val q = MutableBigIntImpl()
    val a = MutableBigIntImpl(mag)
    val b = MutableBigIntImpl(value.mag)
    a.divideKnuth(b, q, false)
    return q.toBigInt(signum * value.signum)
  }

  private fun divideBurnikelZiegler(value: BigIntImpl): BigIntImpl =
    divideAndRemainderBurnikelZiegler(value).first as BigIntImpl

  private fun divideAndRemainderKnuth(value: BigIntImpl): Pair<BigInt, BigInt> {
    val q = MutableBigIntImpl()
    val a = MutableBigIntImpl(mag)
    val b = MutableBigIntImpl(value.mag)
    val r: MutableBigIntImpl = a.divideKnuth(b, q)

    return q.toBigInt(if (signum == value.signum) 1 else -1) to  r.toBigInt(signum.toInt())
  }

  private fun divideAndRemainderBurnikelZiegler(value: BigIntImpl): Pair<BigInt, BigInt> {
    val q = MutableBigIntImpl()
    val r: MutableBigIntImpl = MutableBigIntImpl(this).divideAndRemainderBurnikelZiegler(MutableBigIntImpl(value), q)
    val qBigInt = if (q.isZero()) BigInt.Zero as BigIntImpl else q.toBigInt(signum * value.signum)
    val rBigInt = if (r.isZero()) BigInt.Zero as BigIntImpl else r.toBigInt(signum.toInt())
    return qBigInt to rBigInt
  }

  private fun remainderKnuth(value: BigIntImpl): BigIntImpl {
    val q = MutableBigIntImpl()
    val a = MutableBigIntImpl(mag)
    val b = MutableBigIntImpl(value.mag)
    return a.divideKnuth(b, q).toBigInt(signum.toInt())
  }

  private fun remainderBurnikelZiegler(rhs: BigIntImpl): BigIntImpl =
    divideAndRemainderBurnikelZiegler(rhs).second as BigIntImpl

  private fun getInt(n: Int): Int {
    if (n < 0)
      return 0
    if (n >= mag.size)
      return signInt()

    val magInt = mag[mag.lastIndex - n]

    return if (signum >= 0) magInt else (if (n <= firstNonZeroIntNum()) -magInt else magInt.inv())
  }

  private fun firstNonZeroIntNum(): Int {
    val mLen = mag.size
    var i = mLen - 1

    while (i >= 0 && mag[i] == 0)
      i--

    return mLen - i - 1
  }

  private inline fun signInt() = if (signum < 0) -1 else 0

  private fun shiftRightImpl(n: Int): BigIntImpl {
    val nInts = n ushr 5
    val nBits = n and 0x1F
    val magLen = mag.size
    var newMag: IntArray

    if (nInts >= magLen)
      return (if (signum >= 0) BigInt.Zero else BigInt.NegativeOne) as BigIntImpl

    if (nBits == 0) {
      val newMagLen = magLen - nInts

      if (newMagLen == 0)
        return BigInt.Zero as BigIntImpl

      newMag = mag.copyOf(newMagLen)
    } else {
      var i = 0
      val highBits = mag[0] ushr nBits

      if (highBits != 0) {
        newMag = IntArray(magLen - nInts)
        newMag[i++] = highBits
      } else {
        newMag = IntArray(magLen - nInts - 1)
      }

      if (newMag.isEmpty())
        return BigInt.Zero as BigIntImpl

      val numIter = magLen - nInts - 1

      checkFromToIndex(0, numIter + 1, mag.size)
      checkFromToIndex(i, numIter + i, newMag.size)

      shiftRightImplWorker(newMag, mag, i, nBits, numIter)
    }

    if (signum < 0) {
      var onesLost = false
      var i = magLen - 1
      val j = magLen - nInts

      while (i >= j && !onesLost) {
        onesLost = mag[i] != 0
        i--
      }

      if (!onesLost && nBits != 0)
        onesLost = (mag[magLen - nInts - 1] shl (32 - nBits) != 0)

      if (onesLost)
        newMag = javaIncrement(newMag)
    }

    return BigIntImpl(signum, newMag)
  }

  private inline fun shiftRightImplWorker(newArr: IntArray, oldArr: IntArray, newIdx: Int, shiftCount: Int, numIter: Int) {
    val shiftCountLeft = 32 - shiftCount
    var idx = numIter
    var nidx = if (newIdx == 0) numIter - 1 else numIter

    while (nidx >= newIdx)
      newArr[nidx--] = (oldArr[idx--] ushr  shiftCount) or (oldArr[idx] shl shiftCountLeft)
  }

  private fun square(isRecursion: Boolean): BigInt {
    if (signum == BYTE_ZERO)
      return BigInt.Zero

    val len = mag.size

    if (len < KARATSUBA_SQUARE_THRESHOLD)
      return BigIntImpl(1, trustedStripLeadingZeroInts(squareToLen(mag, len, null)))

    if (len < TOOM_COOK_SQUARE_THRESHOLD)
      return squareKaratsuba()

    if (!isRecursion && bitLength(mag, len) > 16L * MAX_MAG_LENGTH)
      reportOverflow()

    return squareToomCook3()
  }

  private fun times(rhs: BigInt, recursive: Boolean): BigInt {
    if (isZero || rhs.isZero)
      return BigInt.Zero

    val xLen = mag.size

    if (this == rhs && xLen > MULTIPLY_SQUARE_THRESHOLD)
      return square()

    val yLen = (rhs as BigIntImpl).mag.size

    if (xLen < KARATSUBA_THRESHOLD || yLen < KARATSUBA_THRESHOLD) {
      val resultSign = if (signum == rhs.signum) BYTE_ONE else BYTE_NEG_ONE

      if (yLen == 1)
        return multiplyByInt(mag, rhs.mag[0], resultSign)
      if (xLen == 1)
        return multiplyByInt(rhs.mag, mag[0], resultSign)

      return BigIntImpl(resultSign.toByte(), trustedStripLeadingZeroInts(multiplyToLen(mag, xLen, rhs.mag, yLen, null)))
    }

    if (xLen < TOOM_COOK_THRESHOLD && yLen < TOOM_COOK_THRESHOLD)
      return multiplyKaratsuba(this, rhs)

    if (!recursive && bitLength(mag, xLen) + bitLength(rhs.mag, yLen) > 32L * MAX_MAG_LENGTH)
      reportOverflow()

    return multiplyToomCook3(this, rhs)
  }

  private fun compareMagnitude(rhs: BigInt): Byte {
    val m1 = mag
    val len1 = m1.size
    val m2 = (rhs as BigIntImpl).mag
    val len2 = m2.size

    if (len1 < len2)
      return -1
    if (len1 > len2)
      return 1

    for (i in 0 until len1) {
      val a = m1[i].toLong()
      val b = m2[i].toLong()

      if (a != b)
        return if ((a and LONG_MASK) < (b and LONG_MASK)) -1 else 1
    }

    return 0
  }

  private fun getToomSlice(lowerSize: Int, upperSize: Int, slice: Int, fullSize: Int): BigIntImpl {
    var start: Int
    val end: Int

    val len = mag.size
    val offset = fullSize - len

    if (slice == 0) {
      start = 0 - offset
      end = upperSize - 1 - offset
    } else {
      start = upperSize + (slice - 1) * lowerSize - offset
      end = start + lowerSize - 1
    }

    if (start < 0)
      start = 0

    if (end < 0)
      return BigInt.Zero as BigIntImpl

    val sliceSize = (end - start) + 1

    if (sliceSize <= 0)
      return BigInt.Zero as BigIntImpl

    if (start == 0 && sliceSize >= len)
      return abs() as BigIntImpl

    val intSlice = IntArray(sliceSize)
    mag.copyInto(intSlice, 0, start, sliceSize)

    return BigIntImpl(1, trustedStripLeadingZeroInts(intSlice))
  }

  private fun squareKaratsuba(): BigIntImpl {
    val half = (mag.size + 1) / 2
    val xl   = getLower(half)
    val xh   = getUpper(half)
    val xhs  = xh.square()
    val xls  = xl.square()

    return xhs.shl(half*32).plus(xl.plus(xh).square().times(xhs.plus(xls))).shl(half*32).plus(xls) as BigIntImpl
  }

  private fun squareToomCook3(): BigIntImpl {
    val len = mag.size

    // k is the size (in ints) of the lower-order slices.
    val k = (len + 2) / 3

    // r is the size (in ints) of the highest-order slice.
    val r = len - 2 * k

    // Obtain slices of the numbers. a2 is the most significant
    // bits of the number, and a0 the least significant.
    val a0 = getToomSlice(k, r, 2, len)
    val a1 = getToomSlice(k, r, 1, len)
    val a2 = getToomSlice(k, r, 0, len)

    val v0: BigIntImpl
    val v1: BigIntImpl
    val v2: BigIntImpl
    val vm1: BigIntImpl
    val vinf: BigIntImpl
    var t1: BigIntImpl
    var t2: BigIntImpl
    var tm1: BigIntImpl
    var da1: BigIntImpl

    v0 = a0.square(true) as BigIntImpl
    da1 = a2.plus(a0) as BigIntImpl
    vm1 = (da1.minus(a1) as BigIntImpl).square(true) as BigIntImpl
    da1 = da1.plus(a1) as BigIntImpl
    v1 = da1.square(true) as BigIntImpl
    vinf = a2.square(true) as BigIntImpl
    v2 = (da1.plus(a2).shl(1).minus(a0) as BigIntImpl).square(true) as BigIntImpl

    // The algorithm requires two divisions by 2 and one by 3.
    // All divisions are known to be exact, that is, they do not produce
    // remainders, and all results are positive.  The divisions by 2 are
    // implemented as right shifts which are relatively efficient, leaving
    // only a division by 3.
    // The division by 3 is done by an optimized algorithm for this case.
    t2 = (v2.minus(vm1) as BigIntImpl).exactDivideBy3()
    tm1 = v1.minus(vm1).shr(1) as BigIntImpl
    t1 = v1.minus(v0) as BigIntImpl
    t2 = t2.minus(t1).shr(1) as BigIntImpl
    t1 = t1.minus(tm1).minus(vinf) as BigIntImpl
    t2 = t2.minus(vinf.shl(1)) as BigIntImpl
    tm1 = tm1.minus(t2) as BigIntImpl

    // Number of bits to shift left.
    val ss = k * 32

    return vinf.shl(ss).plus(t2).shl(ss).plus(t1).shl(ss).plus(tm1).shl(ss).plus(v0) as BigIntImpl
  }

  private fun exactDivideBy3(): BigIntImpl {
    val len = mag.size
    val result = IntArray(len)
    var x: Long
    var w: Long
    var q: Long
    var borrow = 0L

    for (i in mag.lastIndex downTo 0) {
      x = mag[i].toLong() and LONG_MASK
      w = x - borrow

      borrow = if (borrow > x) 1L else 0L

      q = (w * 0xAAAAAAABL) and LONG_MASK
      result[i] = q.toInt()

      if (q >= 0x55555556L) {
        borrow++

        if (q >= 0xAAAAAAABL)
          borrow++
      }
    }

    return BigIntImpl(signum, trustedStripLeadingZeroInts(result))
  }

  private fun getLower(n: Int): BigIntImpl {
    val len = mag.size

    if (len <= n)
      return abs() as BigIntImpl

    val lowerInts = IntArray(n)
    mag.copyInto(lowerInts, 0, len - n, n)

    return BigIntImpl(1, trustedStripLeadingZeroInts(lowerInts))
  }

  private fun getUpper(n: Int): BigIntImpl {
    val len = mag.size

    if (len <= n)
      return BigInt.Zero as BigIntImpl

    val upperLen = len - n
    val upperInts = IntArray(upperLen)
    mag.copyInto(upperInts, 0, 0, upperLen)

    return BigIntImpl(1, trustedStripLeadingZeroInts(upperInts))
  }

  private fun intLength() = (bitLength() ushr 5) + 1

  override fun equals(other: Any?): Boolean {
    val o = other as? BigIntImpl ?: return false
    return signum == o.signum && mag.contentEquals(o.mag)
  }

  override fun hashCode(): Int {
    var out = 0
    for (i in mag)
      out = (31*out + (i.toLong() and LONG_MASK).toInt())
    return out * signum;
  }

  companion object {
    private fun multiplyToomCook3(a: BigIntImpl, b: BigIntImpl): BigIntImpl {
      val aLen = a.mag.size
      val bLen = b.mag.size

      val largest = max(aLen, bLen)
      val k = (largest + 2) / 3
      val r = largest - 2 * k

      val a2 = a.getToomSlice(k, r, 0, largest)
      val a1 = a.getToomSlice(k, r, 1, largest)
      val a0 = a.getToomSlice(k, r, 2, largest)
      val b2 = b.getToomSlice(k, r, 0, largest)
      val b1 = b.getToomSlice(k, r, 1, largest)
      val b0 = b.getToomSlice(k, r, 2, largest)

      val v0 = a0.times(b0, true) as BigIntImpl
      var da1 = (a2 + a0) as BigIntImpl
      var db1 = (b2 + b0) as BigIntImpl
      val vm1 = (da1.minus(a1) as BigIntImpl).times(db1.minus(b1), true) as BigIntImpl
      da1 = da1.plus(a1) as BigIntImpl
      db1 = db1.plus(b1) as BigIntImpl
      val v1 = da1.times(db1, true) as BigIntImpl
      val v2 = (da1.plus(a2).shl(1).minus(a0) as BigIntImpl).times(db1.plus(b2).shl(1).minus(b0), true) as BigIntImpl
      val vinf = a2.times(b2, true) as BigIntImpl

      var t2 = ((v2 - vm1) as BigIntImpl).exactDivideBy3()
      var tm1 = ((v1 - vm1) shr 1) as BigIntImpl
      var t1 = (v1 - v0) as BigIntImpl
      t2 = t2.minus(t1).shr(1) as BigIntImpl
      t1 = t1.minus(tm1).minus(vinf) as BigIntImpl
      t2 = t2.minus(vinf.shl(1)) as BigIntImpl
      tm1 = tm1.minus(t2) as BigIntImpl

      val sl = k * 32
      val result = vinf.shl(sl).plus(t2).shl(sl).plus(t1).shl(sl).plus(tm1).shl(sl).plus(v0)

      return (if (a.signum != b.signum) -result else result) as BigIntImpl
    }

    private fun multiplyKaratsuba(x: BigIntImpl, y: BigIntImpl): BigIntImpl {
      val xLen = x.mag.size
      val yLen = y.mag.size
      val half = (max(xLen, yLen) + 1) / 2

      val xl = x.getLower(half)
      val xh = x.getUpper(half)
      val yl = y.getLower(half)
      val yh = y.getUpper(half)
      val p1 = xh * yh
      val p2 = xl * yl
      val p3 = (xh + xl) * (yh + yl)

      val result = p1.shl(32 * half).plus(p3.minus(p1).minus(p2)).shl(32 * half).plus(p2)

      return (if (x.signum != y.signum) -result else result) as BigIntImpl
    }

    private inline fun valueOf(value: IntArray): BigIntImpl =
      if (value[0] > 0) BigIntImpl(1, value) else BigIntImpl(value)

    fun valueOf(value: Long): BigIntImpl {
      return if (value == 0L)
        BigInt.Zero as BigIntImpl
      else if (value == -1L)
        BigInt.NegativeOne as BigIntImpl
      else if (value == 1L)
        BigInt.One as BigIntImpl
      else
        BigIntImpl(value)
    }
  }
}

private fun initLogCache(): DoubleArray {
  val out = DoubleArray(37)
  for (i in 2 .. 36)
    out[i] = log(i.toDouble(), E)
  return out
}

private fun initLongRadixCache(): Array<BigIntImpl> {
  return arrayOf(
    BigInt.Zero as BigIntImpl, BigInt.Zero, BigIntImpl.valueOf(0x4000000000000000L),
    BigIntImpl.valueOf(0x383d9170b85ff80bL), BigIntImpl.valueOf(0x4000000000000000L),
    BigIntImpl.valueOf(0x6765c793fa10079dL), BigIntImpl.valueOf(0x41c21cb8e1000000L),
    BigIntImpl.valueOf(0x3642798750226111L), BigIntImpl.valueOf(0x1000000000000000L),
    BigIntImpl.valueOf(0x12bf307ae81ffd59L), BigIntImpl.valueOf(0xde0b6b3a7640000L),
    BigIntImpl.valueOf(0x4d28cb56c33fa539L), BigIntImpl.valueOf(0x1eca170c00000000L),
    BigIntImpl.valueOf(0x780c7372621bd74dL), BigIntImpl.valueOf(0x1e39a5057d810000L),
    BigIntImpl.valueOf(0x5b27ac993df97701L), BigIntImpl.valueOf(0x1000000000000000L),
    BigIntImpl.valueOf(0x27b95e997e21d9f1L), BigIntImpl.valueOf(0x5da0e1e53c5c8000L),
    BigIntImpl.valueOf(0xb16a458ef403f19L), BigIntImpl.valueOf(0x16bcc41e90000000L),
    BigIntImpl.valueOf(0x2d04b7fdd9c0ef49L), BigIntImpl.valueOf(0x5658597bcaa24000L),
    BigIntImpl.valueOf(0x6feb266931a75b7L), BigIntImpl.valueOf(0xc29e98000000000L),
    BigIntImpl.valueOf(0x14adf4b7320334b9L), BigIntImpl.valueOf(0x226ed36478bfa000L),
    BigIntImpl.valueOf(0x383d9170b85ff80bL), BigIntImpl.valueOf(0x5a3c23e39c000000L),
    BigIntImpl.valueOf(0x4e900abb53e6b71L), BigIntImpl.valueOf(0x7600ec618141000L),
    BigIntImpl.valueOf(0xaee5720ee830681L), BigIntImpl.valueOf(0x1000000000000000L),
    BigIntImpl.valueOf(0x172588ad4f5f0981L), BigIntImpl.valueOf(0x211e44f7d02c1000L),
    BigIntImpl.valueOf(0x2ee56725f06e5c71L), BigIntImpl.valueOf(0x41c21cb8e1000000L)
  )
}

private fun initDigitsPerLong(): IntArray =
  intArrayOf(
    0, 0, 62, 39, 31, 27, 24, 22, 20, 19, 18, 18, 17, 17, 16, 16, 15, 15, 15,
    14, 14, 14, 14, 13, 13, 13, 13, 13, 13, 12, 12, 12, 12, 12, 12, 12, 12
  )

private fun initPowerCache(): Array<Array<BigIntImpl?>?> {
  val out = arrayOfNulls<Array<BigIntImpl?>>(37)
  for (i in 2 .. 36)
    out[i] = arrayOf(BigIntImpl.valueOf(i.toLong()))
  return out
}

private fun padWithZeros(sb: StringBuilder, numZeros: Int) {
  var n = numZeros
  while (n >= ZEROES.length) {
    sb.append(ZEROES)
    n -= ZEROES.length
  }

  if (numZeros > 0)
    sb.append(ZEROES, 0, numZeros)
}

private fun getRadixConversionCache(radix: Int, exponent: Int): BigIntImpl {
  var cacheLine = powerCache[radix]!!

  if (exponent < cacheLine.size)
    return cacheLine[exponent]!!

  val oldLength = cacheLine.size
  cacheLine = cacheLine.copyOf(exponent + 1)

  for (i in oldLength .. exponent)
    cacheLine[i] = cacheLine[i - 1]!!.pow(2) as BigIntImpl

  var pc = powerCache
  if (exponent >= pc[radix]!!.size) {
    pc = pc.copyOf()
    pc[radix] = cacheLine
    powerCache = pc
  }

  return cacheLine[exponent]!!
}

private fun makePositive(a: IntArray): IntArray {
  var keep = 0
  while (keep < a.size && a[keep] == -1)
    keep++

  var j = keep
  while (j < a.size && a[j] == 0)
    j++

  val extraInt = if (j == a.size) 1 else 0
  val result = IntArray(a.size - keep + extraInt)

  for (i in keep until a.size)
    result[i - keep + extraInt] = a[i].inv()

  var i = result.lastIndex
  while (++result[i] == 0)
    i--

  return result
}

private fun javaIncrement(value: IntArray): IntArray {
  var lastSum = 0
  var i = value.lastIndex

  while (i >= 0 && lastSum == 0) {
    value[i] += 1
    lastSum = value[i]
    i--
  }

  var v = value
  if (lastSum == 0) {
    v = IntArray(value.size + 1)
    v[0] = 1
  }

  return v
}

private fun squareToLen(x: IntArray, len: Int, tz: IntArray?): IntArray {
  val zLen = len shl 1
  val z = if (tz == null || tz.size < zLen) IntArray(zLen) else tz

  if (len < 1)
    throw IllegalArgumentException("invalid input length: $len")
  if (len > x.size)
    throw IllegalArgumentException("input length out of bounds: $len > ${x.size}")

  if (len * 2 > z.size)
    throw IllegalArgumentException("input length output of bounds: ${len * 2} > ${z.size}")

  if (zLen < 1)
    throw IllegalArgumentException("invalid input length: $zLen")
  if (zLen > z.size)
    throw IllegalArgumentException("input length out of bounds: $zLen > ${z.size}")

  var lastProductLowWord = 0
  var j = 0
  var i = 0

  while (j < len) {
    val piece   = x[j].toLong() and LONG_MASK
    val product = piece * piece
    z[i++] = (lastProductLowWord shl 31) or (product ushr 33).toInt()
    z[i++] = (product ushr 1).toInt()
    lastProductLowWord = product.toInt()
    j++
  }

  var offset = 1
  for (i in len downTo 1) {
    var t = x[i-1]
    t = mulAdd(z, x, offset, i-1, t)
    addOne(z, offset-1, i, t)
    offset += 2
  }

  primitiveLeftShift(z, zLen, 1)
  z[zLen-1] = z[zLen-1] or (x[len-1] and 1)

  return z
}

private fun primitiveLeftShift(a: IntArray, len: Int, n: Int) {
  if (len == 0 || n == 0)
    return

  if (len > a.size)
    throw IndexOutOfBoundsException("len out of bounds: $len > ${a.size}")

  shiftLeftImplWorker(a, a, 0, n, len - 1)

  a[len-1] = a[len-1] shl n
}

private inline fun shiftLeftImplWorker(newArr: IntArray, oldArr: IntArray, newIdx: Int, shiftCount: Int, numIter: Int) {
  val shiftCountRight = 32 - shiftCount
  var n = newIdx
  var oldIdx = 0

  while (oldIdx < numIter)
    newArr[n++] = (oldArr[oldIdx++] shl shiftCount) or (oldArr[oldIdx] ushr shiftCountRight)
}

private inline fun bitLength(value: IntArray, len: Int): Int =
  if (len == 0) 0 else ((len - 1) shl 5) + bitLengthForInt(value[0])

private fun mulAdd(output: IntArray, input: IntArray, offset: Int, len: Int, k: Int): Int {
  if (len > input.size)
    throw IllegalArgumentException("input length is out of bounds: $len > ${input.size}")
  if (offset < 0)
    throw IllegalArgumentException("input offset is invalid: $offset")
  if (offset > output.lastIndex)
    throw IllegalArgumentException("input offset is out of bounds: $offset > ${output.lastIndex}")
  if (len > output.size - offset)
    throw IllegalArgumentException("input len is out of bounds: $len > ${output.size - offset}")

  val kL = k.toLong() and LONG_MASK
  var c  = 0L
  var o  = output.lastIndex - offset
  for (i in len - 1 downTo 0) {
    val product = (input[i].toLong() and LONG_MASK) * kL + (output[o].toLong() and LONG_MASK) + c
    output[o--] = product.toInt()
    c = product ushr 32
  }

  return c.toInt()
}

private fun multiplyByInt(x: IntArray, y: Int, sign: Byte): BigIntImpl {
  if (y.countOneBits() == 1)
    return BigIntImpl(sign, shiftLeft(x, y.countTrailingZeroBits()))

  val xlen = x.size
  var rmag = IntArray(xlen + 1)
  var carry = 0L
  val yl = y.toLong() and LONG_MASK
  var rstart = rmag.size - 1

  for (i in xlen - 1 downTo 0) {
    val product = (x[i].toLong() and LONG_MASK) * yl + carry
    rmag[rstart--] = product.toInt()
    carry = product ushr 32
  }

  if (carry == 0L)
    rmag = rmag.copyOfRange(1, rmag.size)
  else
    rmag[rstart] = carry.toInt()

  return BigIntImpl(sign, rmag)
}

private fun multiplyToLen(x: IntArray, xlen: Int, y: IntArray, ylen: Int, tz: IntArray?): IntArray {
  multiplyToLenCheck(x, xlen)
  multiplyToLenCheck(y, ylen)

  val xStart = xlen - 1
  val yStart = ylen - 1

  val z = tz ?: IntArray(xlen + ylen)

  var carry = 0L
  var j = yStart
  var k = yStart + 1 + xStart

  while (j >= 0) {
    val product = (y[j].toLong() and LONG_MASK) * (x[xStart].toLong() and LONG_MASK) + carry

    z[k] = product.toInt()
    carry = product ushr 32

    j--
    k--
  }

  z[xStart] = carry.toInt()

  for (i in xStart - 1 downTo 0) {
    carry = 0
    j = yStart
    k = yStart + 1 + i

    while (j >= 0) {
      val product = (y[j].toLong() and LONG_MASK) *
        (x[i].toLong() and LONG_MASK) +
        (z[k].toLong() and LONG_MASK) + carry

      z[k] = product.toInt()
      carry = product ushr 32

      j--
      k--
    }

    z[i] = carry.toInt()
  }

  return z
}

private fun multiplyToLenCheck(array: IntArray, length: Int) {
  if (length > array.size)
    throw IndexOutOfBoundsException("array index out of bounds: ${length - 1}")
}

private fun shiftLeft(mag: IntArray, n: Int): IntArray {
  val nInts = n ushr 5
  val nBits = n and 0x1F
  val magLen = mag.size
  val newMag: IntArray

  if (nBits == 0) {
    newMag = IntArray(magLen + nInts)
    mag.copyInto(newMag, 0, 0, magLen)
  } else {
    var i = 0
    val nBits2 = 32 - nBits
    val highBits = mag[0] ushr nBits2

    if (highBits != 0) {
      newMag = IntArray(magLen + nInts + 1)
      newMag[i++] = highBits
    } else {
      newMag = IntArray(magLen + nInts)
    }

    val numIter = magLen - 1
    checkFromToIndex(0, numIter + 1, mag.size)
    checkFromToIndex(i, numIter + i + 1, newMag.size)
    shiftLeftImplWorker(newMag, mag, i, nBits, numIter)
    newMag[numIter + i] = mag[numIter] shl nBits
  }

  return newMag
}

private fun addOne(a: IntArray, offset: Int, mLen: Int, carry: Int): Int {
  var o = a.lastIndex - mLen - offset
  val t = (a[o].toLong() and LONG_MASK) + (carry.toLong() and LONG_MASK)

  a[o] = t.toInt()

  if (t ushr 32 == 0L)
    return 0

  var m = mLen
  while (--m >= 0) {
    if (--o < 0)
      return 1

    a[o]++
    if (a[o] != 0)
      return 0
  }
  return 1
}

private fun add(a: IntArray, b: IntArray): IntArray {
  val x: IntArray
  val y: IntArray

  if (a.size < b.size) {
    x = b
    y = a
  } else {
    x = a
    y = b
  }

  var xIndex = x.size
  var yIndex = y.size
  val result = IntArray(xIndex)
  var sum    = 0L

  if (yIndex == 1) {
    sum = (x[--xIndex].toLong() and LONG_MASK) + (y[0].toLong() and LONG_MASK)
    result[xIndex] = sum.toInt()
  } else {
    while (yIndex > 0) {
      sum = (x[--xIndex].toLong() and LONG_MASK) + (y[--yIndex].toLong() and LONG_MASK) + (sum ushr 32)
      result[xIndex] = sum.toInt()
    }
  }

  var carry = sum ushr 32 != 0L
  while (xIndex > 0 && carry) {
    result[--xIndex] = x[xIndex] + 1
    carry = result[xIndex] == 0
  }

  while (xIndex > 0)
    result[--xIndex] = x[xIndex]

  if (carry) {
    val bigger = IntArray(result.size + 1)
    result.copyInto(bigger, 1)
    bigger[0] = 1
    return bigger
  }

  return result
}

private fun subtract(big: IntArray, little: IntArray): IntArray {
  var bigIndex = big.size
  val result = IntArray(bigIndex)
  var littleIndex = little.size
  var difference = 0L

  while (littleIndex > 0) {
    difference = (big[--bigIndex].toLong() and LONG_MASK) -
      (little[--littleIndex].toLong() and LONG_MASK) +
      (difference shr 32)
    result[bigIndex] = difference.toInt()
  }

  var borrow = difference shr 32 != 0L
  while (bigIndex > 0 && borrow) {
    result[--bigIndex] = big[bigIndex] - 1
    borrow = result[bigIndex] == -1
  }

  while (bigIndex > 0)
    result[--bigIndex] = big[bigIndex]

  return result
}

internal fun trustedStripLeadingZeroInts(v: IntArray): IntArray {
  val vlen = v.size
  var keep = 0

  // Find first nonzero byte

  // Find first nonzero byte
  while (keep < vlen && v[keep] == 0)
    keep++

  return if (keep == 0) v else v.copyOfRange(keep, vlen)
}

internal fun reportOverflow(): Nothing =
  throw ArithmeticException("BigInt would overflow supported range")