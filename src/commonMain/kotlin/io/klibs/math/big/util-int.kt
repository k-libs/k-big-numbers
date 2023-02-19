package io.klibs.math.big

internal inline fun max(a: Int, b: Int) = if (a > b) a else b
internal inline fun max(a: Long, b: Long) = if (a > b) a else b

internal inline fun min(a: Int, b: Int) = if (a < b) a else b

internal inline fun checkFromToIndex(from: Int, to: Int, len: Int): Int {
  if (from < 0 || from > to || to > len)
    throw IndexOutOfBoundsException()

  return from
}

internal inline fun checkFromIndexSize(fromIndex: Int, size: Int, length: Int): Int {
  if (length or fromIndex or size < 0 || size > length - fromIndex)
    throw IndexOutOfBoundsException()

  return fromIndex
}

internal inline fun bitLengthForInt(n: Int) = 32 - n.countLeadingZeroBits()
