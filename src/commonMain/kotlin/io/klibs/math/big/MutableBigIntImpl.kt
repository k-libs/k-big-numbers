package io.klibs.math.big

private const val KNUTH_POW2_THRESH_LEN = 6
private const val KNUTH_POW2_THRESH_ZEROS = 3

internal class MutableBigIntImpl {
  private var value: IntArray
  private var intLen: Int
  private var offset = 0

  constructor() {
    this.value = IntArray(1)
    this.intLen = 0
  }

  constructor(value: Int) {
    this.value = intArrayOf(value)
    this.intLen = 1
  }

  constructor(value: IntArray) {
    this.value = value
    this.intLen = value.size
  }

  constructor(b: BigIntImpl) {
    this.intLen = b.mag.size
    this.value  = b.mag.copyOf(intLen)
  }

  constructor(value: MutableBigIntImpl) {
    this.intLen = value.intLen
    this.value = value.value.copyOfRange(value.offset, value.offset + intLen)
  }

  fun divide(b: MutableBigIntImpl, quotient: MutableBigIntImpl): MutableBigIntImpl =
    divide(b, quotient, true)!!

  fun divide(b: MutableBigIntImpl, quotient: MutableBigIntImpl, needRemainder: Boolean): MutableBigIntImpl? =
    if (b.intLen < BURNIKEL_ZIEGLER_THRESHOLD || intLen - b.intLen < BURNIKEL_ZIEGLER_OFFSET)
      divideKnuth(b, quotient, needRemainder)
    else
      divideAndRemainderBurnikelZiegler(b, quotient)

  fun divideKnuth(b: MutableBigIntImpl, quotient: MutableBigIntImpl): MutableBigIntImpl =
    divideKnuth(b, quotient, true)!!

  fun divideKnuth(ab: MutableBigIntImpl, quotient: MutableBigIntImpl, needRemainder: Boolean): MutableBigIntImpl? {
    var b = ab
    if (b.intLen == 0)
      throw ArithmeticException("BigInteger divide by zero")

    // Dividend is zero

    // Dividend is zero
    if (intLen == 0) {
      quotient.offset = 0
      quotient.intLen = quotient.offset
      return if (needRemainder) MutableBigIntImpl() else null
    }

    val cmp: Int = compareTo(b)
    // Dividend less than divisor
    // Dividend less than divisor
    if (cmp < 0) {
      quotient.offset = 0
      quotient.intLen = quotient.offset
      return if (needRemainder) MutableBigIntImpl(this) else null
    }
    // Dividend equal to divisor
    // Dividend equal to divisor
    if (cmp == 0) {
      quotient.intLen = 1
      quotient.value[0] = quotient.intLen
      quotient.offset = 0
      return if (needRemainder) MutableBigIntImpl() else null
    }

    quotient.clear()
    // Special case one word divisor
    // Special case one word divisor
    if (b.intLen == 1) {
      val r = divideOneWord(b.value.get(b.offset), quotient)

      return if (needRemainder) {
        if (r == 0) MutableBigIntImpl() else MutableBigIntImpl(r)
      } else {
        null
      }
    }

    // Cancel common powers of two if we're above the KNUTH_POW2_* thresholds

    // Cancel common powers of two if we're above the KNUTH_POW2_* thresholds
    if (intLen >= KNUTH_POW2_THRESH_LEN) {
      val trailingZeroBits: Int = min(getLowestSetBit(), b.getLowestSetBit())

      if (trailingZeroBits >= KNUTH_POW2_THRESH_ZEROS * 32) {
        val a = MutableBigIntImpl(this)
        b = MutableBigIntImpl(b)
        a.rightShift(trailingZeroBits)
        b.rightShift(trailingZeroBits)
        val r: MutableBigIntImpl = a.divideKnuth(b, quotient)
        r.leftShift(trailingZeroBits)
        return r
      }
    }

    return divideMagnitude(b, quotient, needRemainder)
  }

  fun divideAndRemainderBurnikelZiegler(b: MutableBigIntImpl, quotient: MutableBigIntImpl): MutableBigIntImpl {
    val r = intLen
    val s: Int = b.intLen

    quotient.offset = 0
    quotient.intLen = 0

    return if (r < s) {
      this
    } else {
      val m = 1 shl 32 - (s / BURNIKEL_ZIEGLER_THRESHOLD).countLeadingZeroBits()
      val j = (s + m - 1) / m
      val n = j * m
      val n32 = 32L * n
      val sigma = max(0L, n32 - b.bitLength()).toInt()
      val bShifted = MutableBigIntImpl(b)
      bShifted.safeLeftShift(sigma)
      val aShifted = MutableBigIntImpl(this)
      aShifted.safeLeftShift(sigma)

      var t: Int = ((aShifted.bitLength() + n32) / n32).toInt()
      if (t < 2) {
        t = 2
      }

      val a1: MutableBigIntImpl = aShifted.getBlock(t - 1, t, n)

      var z: MutableBigIntImpl = aShifted.getBlock(t - 2, t, n)
      z.addDisjoint(a1, n) // z[t-2]

      val qi = MutableBigIntImpl()
      var ri: MutableBigIntImpl
      for (i in t - 2 downTo 1) {
        ri = z.divide2n1n(bShifted, qi)

        z = aShifted.getBlock(i - 1, t, n)
        z.addDisjoint(ri, n)
        quotient.addShifted(qi, i * n)
      }
      ri = z.divide2n1n(bShifted, qi)
      quotient.add(qi)
      ri.rightShift(sigma)
      ri
    }
  }

  fun divideOneWord(divisor: Int, quotient: MutableBigIntImpl): Int {
    val divisorLong = divisor.toLong() and LONG_MASK

    if (intLen == 1) {
      val dividendValue = value[offset].toLong() and LONG_MASK
      val q = (dividendValue / divisorLong).toInt()
      val r = (dividendValue - q * divisorLong).toInt()
      quotient.value[0] = q
      quotient.intLen = if (q == 0) 0 else 1
      quotient.offset = 0
      return r
    }

    if (quotient.value.size < intLen) quotient.value = IntArray(intLen)
    quotient.offset = 0
    quotient.intLen = intLen

    val shift: Int = divisor.countLeadingZeroBits()

    var rem = value[offset]
    var remLong = rem.toLong() and LONG_MASK
    if (remLong < divisorLong) {
      quotient.value[0] = 0
    } else {
      quotient.value[0] = (remLong / divisorLong).toInt()
      rem = (remLong - quotient.value[0] * divisorLong).toInt()
      remLong = rem.toLong() and LONG_MASK
    }
    var xlen = intLen
    while (--xlen > 0) {
      val dividendEstimate = remLong shl 32 or
        (value[offset + intLen - xlen].toLong() and LONG_MASK)
      var q: Int
      if (dividendEstimate >= 0) {
        q = (dividendEstimate / divisorLong).toInt()
        rem = (dividendEstimate - q * divisorLong).toInt()
      } else {
        val tmp: Long = divWord(dividendEstimate, divisor)
        q = (tmp and LONG_MASK).toInt()
        rem = (tmp ushr 32).toInt()
      }
      quotient.value[intLen - xlen] = q
      remLong = rem.toLong() and LONG_MASK
    }

    quotient.normalize()
    return if (shift > 0) rem % divisor else rem
  }

  fun divide2n1n(b: MutableBigIntImpl, quotient: MutableBigIntImpl): MutableBigIntImpl {
    val n = b.intLen

    if (n % 2 != 0 || n < BURNIKEL_ZIEGLER_THRESHOLD) {
      return divideKnuth(b, quotient)
    }

    val aUpper = MutableBigIntImpl(this)
    aUpper.safeRightShift(32 * (n / 2))

    keepLower(n / 2)


    val q1 = MutableBigIntImpl()
    val r1: MutableBigIntImpl = aUpper.divide3n2n(b, q1)

    addDisjoint(r1, n / 2)

    val r2: MutableBigIntImpl = divide3n2n(b, quotient)

    quotient.addDisjoint(q1, n / 2)
    return r2
  }

  fun divide3n2n(b: MutableBigIntImpl, quotient: MutableBigIntImpl): MutableBigIntImpl {
    val n = b.intLen / 2

    val a12 = MutableBigIntImpl(this)
    a12.safeRightShift(32 * n)

    val b1 = MutableBigIntImpl(b)
    b1.safeRightShift(n * 32)
    val b2: BigIntImpl = b.getLower(n)

    val r: MutableBigIntImpl
    val d: MutableBigIntImpl
    if (compareShifted(b, n) < 0) {
      r = a12.divide2n1n(b1, quotient)

      d = MutableBigIntImpl(quotient.toBigInt().times(b2) as BigIntImpl)
    } else {
      quotient.ones(n)
      a12.add(b1)
      b1.leftShift(32 * n)
      a12.subtract(b1)
      r = a12

      d = MutableBigIntImpl(b2)
      d.leftShift(32 * n)
      d.subtract(MutableBigIntImpl(b2))
    }

    r.leftShift(32 * n)
    r.addLower(this, n)

    while (r < d) {
      r.add(b)
      quotient.subtract(MutableBigIntImpl(1))
    }
    r.subtract(d)

    return r
  }

  fun divideMagnitude(div: MutableBigIntImpl, quotient: MutableBigIntImpl, needRemainder: Boolean): MutableBigIntImpl? {
    val shift: Int = div.value[div.offset].countLeadingZeroBits()
    val dlen = div.intLen
    val divisor: IntArray
    val rem: MutableBigIntImpl

    if (shift > 0) {
      divisor = IntArray(dlen)
      copyAndShift(div.value, div.offset, dlen, divisor, 0, shift)
      if (value[offset].countLeadingZeroBits() >= shift) {
        val remarr = IntArray(intLen + 1)
        rem = MutableBigIntImpl(remarr)
        rem.intLen = intLen
        rem.offset = 1
        copyAndShift(value, offset, intLen, remarr, 1, shift)
      } else {
        val remarr = IntArray(intLen + 2)
        rem = MutableBigIntImpl(remarr)
        rem.intLen = intLen + 1
        rem.offset = 1
        var rFrom = offset
        var c = 0
        val n2 = 32 - shift
        var i = 1
        while (i < intLen + 1) {
          val b = c
          c = value[rFrom]
          remarr[i] = b shl shift or (c ushr n2)
          i++
          rFrom++
        }
        remarr[intLen + 1] = c shl shift
      }
    } else {
      divisor = div.value.copyOfRange(div.offset, div.offset + div.intLen)
      rem = MutableBigIntImpl(IntArray(intLen + 1))
      value.copyInto(rem.value, 1, offset, intLen)
      rem.intLen = intLen
      rem.offset = 1
    }

    val nlen: Int = rem.intLen

    val limit = nlen - dlen + 1
    if (quotient.value.size < limit) {
      quotient.value = IntArray(limit)
      quotient.offset = 0
    }
    quotient.intLen = limit
    val q = quotient.value

    if (rem.intLen == nlen) {
      rem.offset = 0
      rem.value[0] = 0
      rem.intLen++
    }

    val dh = divisor[0]
    val dhLong = dh.toLong() and LONG_MASK
    val dl = divisor[1]

    for (j in 0 until limit - 1) {
      var qhat = 0
      var qrem = 0
      var skipCorrection = false
      val nh: Int = rem.value.get(j + rem.offset)
      val nh2 = nh + -0x80000000
      val nm: Int = rem.value.get(j + 1 + rem.offset)
      if (nh == dh) {
        qhat = 0.inv()
        qrem = nh + nm
        skipCorrection = qrem + -0x80000000 < nh2
      } else {
        val nChunk = nh.toLong() shl 32 or (nm.toLong() and LONG_MASK)
        if (nChunk >= 0) {
          qhat = (nChunk / dhLong).toInt()
          qrem = (nChunk - qhat * dhLong).toInt()
        } else {
          val tmp: Long = divWord(nChunk, dh)
          qhat = (tmp and LONG_MASK).toInt()
          qrem = (tmp ushr 32).toInt()
        }
      }
      if (qhat == 0) continue
      if (!skipCorrection) {
        val nl: Long = rem.value.get(j + 2 + rem.offset).toLong() and LONG_MASK
        var rs = qrem.toLong() and LONG_MASK shl 32 or nl
        var estProduct = (dl.toLong() and LONG_MASK) * (qhat.toLong() and LONG_MASK)
        if (unsignedLongCompare(estProduct, rs)) {
          qhat--
          qrem = ((qrem.toLong() and LONG_MASK) + dhLong).toInt()
          if (qrem.toLong() and LONG_MASK >= dhLong) {
            estProduct -= dl.toLong() and LONG_MASK
            rs = qrem.toLong() and LONG_MASK shl 32 or nl
            if (unsignedLongCompare(estProduct, rs)) qhat--
          }
        }
      }

      rem.value[j + rem.offset] = 0
      val borrow = mulsub(rem.value, divisor, qhat, dlen, j + rem.offset)

      if (borrow + -0x80000000 > nh2) {
        divadd(divisor, rem.value, j + 1 + rem.offset)
        qhat--
      }

      q[j] = qhat
    }

    var qhat = 0
    var qrem = 0
    var skipCorrection = false
    val nh: Int = rem.value.get(limit - 1 + rem.offset)
    val nh2 = nh + -0x80000000
    val nm: Int = rem.value.get(limit + rem.offset)

    if (nh == dh) {
      qhat = 0.inv()
      qrem = nh + nm
      skipCorrection = qrem + -0x80000000 < nh2
    } else {
      val nChunk = nh.toLong() shl 32 or (nm.toLong() and LONG_MASK)
      if (nChunk >= 0) {
        qhat = (nChunk / dhLong).toInt()
        qrem = (nChunk - qhat * dhLong).toInt()
      } else {
        val tmp: Long = divWord(nChunk, dh)
        qhat = (tmp and LONG_MASK).toInt()
        qrem = (tmp ushr 32).toInt()
      }
    }
    if (qhat != 0) {
      if (!skipCorrection) {
        val nl: Long = rem.value.get(limit + 1 + rem.offset).toLong() and LONG_MASK
        var rs = qrem.toLong() and LONG_MASK shl 32 or nl
        var estProduct = (dl.toLong() and LONG_MASK) * (qhat.toLong() and LONG_MASK)
        if (unsignedLongCompare(estProduct, rs)) {
          qhat--
          qrem = ((qrem.toLong() and LONG_MASK) + dhLong).toInt()
          if (qrem.toLong() and LONG_MASK >= dhLong) {
            estProduct -= dl.toLong() and LONG_MASK
            rs = qrem.toLong() and LONG_MASK shl 32 or nl
            if (unsignedLongCompare(estProduct, rs)) qhat--
          }
        }
      }

      val borrow: Int
      rem.value[limit - 1 + rem.offset] = 0
      borrow = if (needRemainder)
        mulsub(rem.value, divisor, qhat, dlen, limit - 1 + rem.offset)
      else
        mulsubBorrow(rem.value, divisor, qhat, dlen, limit - 1 + rem.offset)

      if (borrow + -0x80000000 > nh2) {
        if (needRemainder) divadd(divisor, rem.value, limit - 1 + 1 + rem.offset)
        qhat--
      }

      // Store the quotient digit
      q[limit - 1] = qhat
    }


    if (needRemainder) {
      if (shift > 0) rem.rightShift(shift)
      rem.normalize()
    }
    quotient.normalize()
    return if (needRemainder) rem else null
  }

  fun getBlock(index: Int, numBlocks: Int, blockLength: Int): MutableBigIntImpl {
    val blockStart = index * blockLength

    if (blockStart >= intLen)
      return MutableBigIntImpl()

    val blockEnd = if (index == numBlocks - 1) intLen else (index + 1) * blockLength

    if (blockEnd > intLen)
      return MutableBigIntImpl()

    return MutableBigIntImpl(value.copyOfRange(offset + intLen - blockEnd, offset + intLen - blockStart))
  }

  fun getLowestSetBit(): Int {
    if (intLen == 0)
      return -1

    var j = intLen - 1
    while (j > 0 && value[j+offset] == 0)
      j--

    val b = value[j + offset]
    if (b == 0)
      return -1

    return ((intLen - 1 - j) shl 5) + b.countTrailingZeroBits()
  }

  fun bitLength() = if (intLen == 0) 0L else 32L * intLen - value[offset].countLeadingZeroBits()

  fun clear() {
    offset = 0
    intLen = 0

    for (i in 0 .. value.lastIndex)
      value[i] = 0
  }

  fun reset() {
    offset = 0
    intLen = 0
  }

  fun safeRightShift(n: Int) {
    if (n / 32 >= intLen)
      reset()
    else
      rightShift(n)
  }

  fun rightShift(n: Int) {
    if (intLen == 0)
      return

    val nInts = n ushr 5
    val nBits = n and 0x1F

    intLen -= nInts

    if (nBits == 0)
      return

    val bitsInHighWord = bitLengthForInt(value[offset])

    if (nBits >= bitsInHighWord) {
      primitiveLeftShift(32 - nBits)
      intLen--
    } else {
      primitiveRightShift(nBits)
    }
  }

  fun primitiveRightShift(n: Int) {
    val value = this.value
    val n2 = 32 - n
    var i = offset + intLen - 1
    var c = value[i]

    while (i > offset) {
      val b = c
      c = value[i - 1]
      value[i] = (c shl n2) or (b ushr n)
      i--
    }

    value[offset] = value[offset] ushr n
  }

  fun primitiveLeftShift(n: Int) {
    val value = this.value
    val n2 = 32 - n
    var i = offset
    var c = value[i]
    val m = i + intLen - 1

    while (i < m) {
      val b = c
      c = value[i + 1]
      value[i] = (b shl n) or (c ushr n2)
      i++
    }

    value[offset + intLen - 1] = value[offset + intLen - 1] shl n
  }

  fun safeLeftShift(n: Int) {
    if (n > 0)
      leftShift(n)
  }

  fun leftShift(n: Int) {
    if (intLen == 0)
      return

    val nInts = n ushr 5
    val nBits = n and 0x1F
    val bitsInHighWord = bitLengthForInt(value[offset])

    if (n <= 32 - bitsInHighWord) {
      primitiveLeftShift(nBits)
      return
    }

    var newLen = intLen + nInts + 1
    if (nBits <= 32 - bitsInHighWord)
      newLen--

    if (value.size < newLen) {
      val result = IntArray(newLen)

      for (i in 0 until intLen)
        result[i] = value[offset + i]

      setValue(result, newLen)
    } else if (value.size - offset >= newLen) {
      for (i in 0 until newLen - intLen)
        value[offset + intLen + i] = 0
    } else {
      for (i in 0 until intLen)
        value[i] = value[offset + i]

      for (i in intLen until newLen)
        value[i] = 0

      offset = 0
    }

    intLen = newLen

    if (nBits == 0)
      return

    if (nBits <= 32 - bitsInHighWord)
      primitiveLeftShift(nBits)
    else
      primitiveRightShift(32 - nBits)
  }

  fun normalize() {
    if (intLen == 0) {
      offset = 0
      return
    }

    var index = offset
    if (value[index] != 0)
      return

    val indexBound = index + intLen

    do {
      index++
    } while (index < indexBound && value[index] == 0)

    val numZeros = index - offset
    intLen -= numZeros
    offset = if (intLen == 0) 0 else offset + numZeros
  }

  fun setValue(value: IntArray, length: Int) {
    this.value = value
    this.intLen = length
    this.offset = 0
  }

  fun ones(n: Int) {
    if (n > value.size)
      value = IntArray(n)

    for (i in 0 .. value.lastIndex)
      value[i] = -1

    offset = 0
    intLen = n
  }

  fun subtract(tb: MutableBigIntImpl): Int {
    var a = this
    var b = tb

    var result = value
    var sign = a.compareTo(b)

    if (sign == 0) {
      reset()
      return 0
    }

    if (sign < 0) {
      val tmp = a
      a = b
      b = tmp
    }

    val resultLen = a.intLen
    if (result.size < resultLen)
      result = IntArray(resultLen)

    var diff = 0L
    var x = a.intLen
    var y = b.intLen
    var rStart = result.size - 1

    while (y > 0) {
      x--; y--

      diff = (a.value[x+a.offset].toLong() and LONG_MASK) -
        (b.value[y+b.offset].toLong() and LONG_MASK) - (-(diff shr 32)).toInt()

      result[rStart--] = diff.toInt()
    }

    while (x > 0) {
      x--
      diff = (a.value[x+a.offset].toLong() and LONG_MASK) - (-(diff shr 32)).toInt()
      result[rStart--] = diff.toInt()
    }

    value = result
    intLen = resultLen
    offset = value.size - resultLen

    normalize()

    return sign
  }

  fun compareShifted(b: MutableBigIntImpl, ints: Int): Int {
    val blen = b.intLen
    val alen = intLen - ints

    if (alen < blen)
      return -1
    if (alen > blen)
      return 1

    val bval = b.value
    var i = offset
    var j = b.offset
    while (i < alen + offset) {
      val b1 = value[i] + 0x80000000
      val b2 = bval[j] + 0x80000000

      if (b1 < b2)
        return -1
      if (b1 > b2)
        return 1

      i++
      j++
    }

    return 0
  }

  fun isZero() = intLen == 0

  fun add(addend: MutableBigIntImpl) {
    var x = intLen
    var y = addend.intLen
    var resultLen = if (intLen > addend.intLen) intLen else addend.intLen
    var result = if (value.size < resultLen) IntArray(resultLen) else value

    var rstart = result.size - 1
    var sum: Long
    var carry: Long = 0

    // Add common parts of both numbers

    // Add common parts of both numbers
    while (x > 0 && y > 0) {
      x--
      y--
      sum = (value[x + offset].toLong() and LONG_MASK) +
        (addend.value[y + addend.offset].toLong() and LONG_MASK) + carry
      result[rstart--] = sum.toInt()
      carry = sum ushr 32
    }

    // Add remainder of the longer number

    // Add remainder of the longer number
    while (x > 0) {
      x--
      if (carry == 0L && result == value && rstart == x + offset) return
      sum = (value[x + offset].toLong() and LONG_MASK) + carry
      result[rstart--] = sum.toInt()
      carry = sum ushr 32
    }
    while (y > 0) {
      y--
      sum = (addend.value[y + addend.offset].toLong() and LONG_MASK) + carry
      result[rstart--] = sum.toInt()
      carry = sum ushr 32
    }

    if (carry > 0) { // Result must grow in length
      resultLen++
      if (result.size < resultLen) {
        val temp = IntArray(resultLen)
        // Result one word longer from carry-out; copy low-order
        // bits into new result.
        result.copyInto(temp, 1, 0, result.size)
        temp[0] = 1
        result = temp
      } else {
        result[rstart--] = 1
      }
    }

    value = result
    intLen = resultLen
    offset = result.size - resultLen
  }

  fun addDisjoint(addend: MutableBigIntImpl, n: Int) {
    if (addend.isZero())
      return

    val x = intLen
    var y = addend.intLen + n
    val resultLen = if (intLen > y) intLen else y
    val result: IntArray

    if (value.size < resultLen) {
      result = IntArray(resultLen)
    } else {
      result = value
      value.fill(0, offset + intLen, value.size)
    }

    var rStart = result.size - 1

    value.copyInto(result, rStart + 1 - x, offset, x)
    y -= x
    rStart -= x

    val len: Int = min(y, addend.value.size - addend.offset)
    addend.value.copyInto(result, rStart + 1 - y, addend.offset, len)

    // zero the gap

    // zero the gap
    for (i in rStart + 1 - y + len until rStart + 1) result[i] = 0

    value = result
    intLen = resultLen
    offset = result.size - resultLen
  }

  fun addShifted(addend: MutableBigIntImpl, n: Int) {
    if (addend.isZero()) {
      return
    }

    var x = intLen
    var y = addend.intLen + n
    var resultLen = if (intLen > y) intLen else y
    var result = if (value.size < resultLen) IntArray(resultLen) else value

    var rstart = result.size - 1
    var sum: Long
    var carry: Long = 0

    // Add common parts of both numbers

    // Add common parts of both numbers
    while (x > 0 && y > 0) {
      x--
      y--
      val bval = if (y + addend.offset < addend.value.size) addend.value[y + addend.offset] else 0
      sum = (value[x + offset].toLong() and LONG_MASK) +
        (bval.toLong() and LONG_MASK) + carry
      result[rstart--] = sum.toInt()
      carry = sum ushr 32
    }

    // Add remainder of the longer number

    // Add remainder of the longer number
    while (x > 0) {
      x--
      if (carry == 0L && result == value && rstart == x + offset) {
        return
      }
      sum = (value[x + offset].toLong() and LONG_MASK) + carry
      result[rstart--] = sum.toInt()
      carry = sum ushr 32
    }
    while (y > 0) {
      y--
      val bval = if (y + addend.offset < addend.value.size) addend.value[y + addend.offset] else 0
      sum = (bval.toLong() and LONG_MASK) + carry
      result[rstart--] = sum.toInt()
      carry = sum ushr 32
    }

    if (carry > 0) { // Result must grow in length
      resultLen++
      if (result.size < resultLen) {
        val temp = IntArray(resultLen)
        // Result one word longer from carry-out; copy low-order
        // bits into new result.
        result.copyInto(temp, 1, 0, result.size)
        temp[0] = 1
        result = temp
      } else {
        result[rstart--] = 1
      }
    }

    value = result
    intLen = resultLen
    offset = result.size - resultLen

  }

  fun keepLower(n: Int) {
    if (intLen >= n) {
      offset += intLen - n
      intLen = n
    }
  }

  fun getLower(n: Int): BigIntImpl {
    if (isZero()) {
      return BigInt.Zero as BigIntImpl
    } else if (intLen < n) {
      return toBigInt(1)
    } else {
      var len = n
      while (len > 0 && value[offset + intLen - len] == 0) len--
      val sign = if (len > 0) 1 else 0
      return BigIntImpl(sign.toByte(), value.copyOfRange(offset + intLen - len, offset + intLen))
    }
  }

  fun addLower(addend: MutableBigIntImpl, n: Int) {
    val a = MutableBigIntImpl(addend)
    if (a.offset + a.intLen >= n) {
      a.offset = a.offset + a.intLen - n
      a.intLen = n
    }
    a.normalize()
    add(a)
  }

  operator fun compareTo(b: MutableBigIntImpl): Int {
    if (intLen < b.intLen)
      return -1
    if (intLen > b.intLen)
      return 1

    var i = offset
    var j = b.offset
    while (i < intLen + offset) {
      val b1 = value[i] + 0x80000000
      val b2 = b.value[j] + 0x80000000

      if (b1 < b2)
        return -1
      if (b1 > b2)
        return 1

      i++
      j++
    }

    return 0
  }

  fun toBigInt(sign: Int): BigIntImpl {
    return if (intLen == 0 || sign == 0)
      BigInt.Zero as BigIntImpl
    else
      BigIntImpl(sign.toByte(), getMagnitudeArray())
  }

  fun toBigInt(): BigIntImpl {
    normalize()
    return toBigInt(if (isZero()) 0 else 1)
  }

  private fun getMagnitudeArray(): IntArray {
    if (offset > 0 || value.size != intLen) {
      val tmp: IntArray = value.copyOfRange(offset, offset + intLen)
      value.fill(0)
      offset = 0
      intLen = tmp.size
      value = tmp
    }
    return value
  }

  private fun mulsub(q: IntArray, a: IntArray, x: Int, len: Int, offset: Int): Int {
    val xLong = x.toLong() and LONG_MASK
    var carry = 0L
    var o = offset + len

    for (j in len - 1 downTo 0) {
      val product    = (a[j].toLong() and LONG_MASK) * xLong + carry
      val difference = q[o] - product
      q[o--] = difference.toInt()
      carry = (product ushr 32) + if (difference and LONG_MASK > product.toInt().inv().toLong() and LONG_MASK) 1 else 0
    }

    return carry.toInt()
  }

  private fun mulsubBorrow(q: IntArray, a: IntArray, x: Int, len: Int, offset: Int): Int {
    val xLong = x.toLong() and LONG_MASK
    var carry: Long = 0
    var o = offset + len

    for (j in len - 1 downTo 0) {
      val product = (a[j].toLong() and LONG_MASK) * xLong + carry
      val difference = q[o--] - product
      carry = ((product ushr 32) + if (difference and LONG_MASK > product.toInt().inv().toLong() and LONG_MASK) 1 else 0)
    }

    return carry.toInt()
  }

  private fun divadd(a: IntArray, result: IntArray, offset: Int): Int {
    var carry = 0L

    for (j in a.lastIndex downTo 0) {
      val sum = (a[j].toLong() and LONG_MASK) +
        (result[j + offset].toLong() and LONG_MASK) +
        carry

      result[j + offset] = sum.toInt()
      carry = sum ushr 32
    }

    return carry.toInt()
  }

  private fun divWord(n: Long, d: Int): Long {
    val dLong = d.toLong() and LONG_MASK
    var r: Long
    var q: Long

    if (dLong == 1L) {
      q = n.toInt().toLong()
      r = 0
      return (r shl 32) or (q and LONG_MASK)
    }

    q = (n ushr 1) / (dLong ushr 1)
    r = n - q * dLong

    while (r < 0) {
      r += dLong
      q--
    }

    while (r >= dLong) {
      r -= dLong
      q++
    }

    return (r shl 32) or (q and LONG_MASK)
  }
}

private fun copyAndShift(src: IntArray, srcFrom: Int, srcLen: Int, dst: IntArray, dstFrom: Int, shift: Int) {
  var srcFrom = srcFrom
  val n2 = 32 - shift
  var c = src[srcFrom]
  for (i in 0 until srcLen - 1) {
    val b = c
    c = src[++srcFrom]
    dst[dstFrom + i] = b shl shift or (c ushr n2)
  }
  dst[dstFrom + srcLen - 1] = c shl shift
}

private fun unsignedLongCompare(one: Long, two: Long) =
  (one + Long.MIN_VALUE) > (two + Long.MIN_VALUE)