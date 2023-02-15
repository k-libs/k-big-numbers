package io.klibs.math.big

internal inline fun max(a: Int, b: Int) = if (a > b) a else b

internal inline fun checkFromToIndex(from: Int, to: Int, len: Int): Int {
  if (from < 0 || from > to || to > len)
    throw IndexOutOfBoundsException()

  return from
}