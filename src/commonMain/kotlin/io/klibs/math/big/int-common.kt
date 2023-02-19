package io.klibs.math.big

import kotlin.math.E
import kotlin.math.log
import kotlin.math.log2

internal const val MULTIPLY_SQUARE_THRESHOLD = 20

internal const val KARATSUBA_THRESHOLD = 80
internal const val KARATSUBA_SQUARE_THRESHOLD = 128

internal const val TOOM_COOK_THRESHOLD = 240
internal const val TOOM_COOK_SQUARE_THRESHOLD = 216

internal const val SCHOENHAGE_BASE_CONVERSION_THRESHOLD = 20

internal const val ZEROES = "0000000000000000000000000000000000000000000000000000000000000000"

internal val LOG_TWO = log2(2.0)

internal var powerCache = initPowerCache()
internal val logCache by lazy { initLogCache() }
internal val digitsPerLong by lazy { initDigitsPerLong() }
internal val longRadix by lazy { initLongRadixCache() }


internal fun initLogCache(): DoubleArray {
  val out = DoubleArray(37)
  for (i in 2 .. 36)
    out[i] = log(i.toDouble(), E)
  return out
}

internal fun initLongRadixCache(): Array<BigIntImpl> {
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

internal fun initDigitsPerLong(): IntArray =
  intArrayOf(
    0, 0, 62, 39, 31, 27, 24, 22, 20, 19, 18, 18, 17, 17, 16, 16, 15, 15, 15,
    14, 14, 14, 14, 13, 13, 13, 13, 13, 13, 12, 12, 12, 12, 12, 12, 12, 12
  )

internal fun initPowerCache(): Array<Array<BigIntImpl?>?> {
  val out = arrayOfNulls<Array<BigIntImpl?>>(37)
  for (i in 2 .. 36)
    out[i] = arrayOf(BigIntImpl.valueOf(i.toLong()))
  return out
}

internal fun padWithZeros(sb: StringBuilder, numZeros: Int) {
  var n = numZeros
  while (n >= ZEROES.length) {
    sb.append(ZEROES)
    n -= ZEROES.length
  }

  if (numZeros > 0)
    sb.append(ZEROES, 0, numZeros)
}

internal fun getRadixConversionCache(radix: Int, exponent: Int): BigIntImpl {
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

internal fun makePositive(a: IntArray): IntArray {
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

internal fun javaIncrement(value: IntArray): IntArray {
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

internal fun squareToLen(x: IntArray, len: Int, tz: IntArray?): IntArray {
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

internal fun primitiveLeftShift(a: IntArray, len: Int, n: Int) {
  if (len == 0 || n == 0)
    return

  if (len > a.size)
    throw IndexOutOfBoundsException("len out of bounds: $len > ${a.size}")

  shiftLeftImplWorker(a, a, 0, n, len - 1)

  a[len-1] = a[len-1] shl n
}

internal inline fun shiftLeftImplWorker(newArr: IntArray, oldArr: IntArray, newIdx: Int, shiftCount: Int, numIter: Int) {
  val shiftCountRight = 32 - shiftCount
  var n = newIdx
  var oldIdx = 0

  while (oldIdx < numIter)
    newArr[n++] = (oldArr[oldIdx++] shl shiftCount) or (oldArr[oldIdx] ushr shiftCountRight)
}

internal inline fun bitLength(value: IntArray, len: Int): Int =
  if (len == 0) 0 else ((len - 1) shl 5) + bitLengthForInt(value[0])

internal fun mulAdd(output: IntArray, input: IntArray, offset: Int, len: Int, k: Int): Int {
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

internal fun multiplyByInt(x: IntArray, y: Int, sign: Byte): BigIntImpl {
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

internal fun multiplyToLen(x: IntArray, xlen: Int, y: IntArray, ylen: Int, tz: IntArray?): IntArray {
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

internal fun multiplyToLenCheck(array: IntArray, length: Int) {
  if (length > array.size)
    throw IndexOutOfBoundsException("array index out of bounds: ${length - 1}")
}

internal fun shiftLeft(mag: IntArray, n: Int): IntArray {
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

internal fun addOne(a: IntArray, offset: Int, mLen: Int, carry: Int): Int {
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

internal fun add(a: IntArray, b: IntArray): IntArray {
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

internal fun subtract(big: IntArray, little: IntArray): IntArray {
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