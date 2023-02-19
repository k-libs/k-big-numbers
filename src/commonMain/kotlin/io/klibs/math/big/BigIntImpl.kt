package io.klibs.math.big

import kotlin.math.*


internal class BigIntImpl : BigInt {

  internal val signum: Byte
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

    val cmp = compareMagnitude(rhs as BigIntImpl)
    if (cmp == BYTE_ZERO)
      return BigInt.Zero

    val resultMag = trustedStripLeadingZeroInts(
      if (cmp > 0)
        subtract(mag, rhs.mag)
      else
        subtract(rhs.mag, mag)
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

    val cmp = compareMagnitude(rhs as BigIntImpl)
    if (cmp == BYTE_ZERO)
      return BigInt.Zero

    val result = trustedStripLeadingZeroInts(
      if (cmp > 0)
        subtract(mag, rhs.mag)
      else
        subtract(rhs.mag, mag)
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

  internal fun compareMagnitude(rhs: BigIntImpl): Byte {
    val m1 = mag
    val len1 = m1.size
    val m2 = rhs.mag
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

  private fun intLength() = bitLength() ushr 5

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
