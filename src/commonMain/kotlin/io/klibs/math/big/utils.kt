package io.klibs.math.big

import io.klibs.collections.UIntDeque

internal inline fun max(a: Int, b: Int) = if (a > b) a else b
internal inline fun min(a: Int, b: Int) = if (a < b) a else b
internal inline fun abs(a: Byte) = if (a < 0) (-a).toByte() else a
internal inline fun abs(i: Int) = if (i < 0) -i else i

internal inline fun trimToSize(b: UIntDeque) {
  while (b.size > 0 && b.peekFirst() == 0u)
    b.popFirst()
  b.trimToSize()
}


internal inline fun Char.toBase8Digit(): Int =
  this - '0'
internal inline fun Char.isBase8Digit(): Boolean =
  this in '0' .. '7'

internal inline fun Char.toBase10Digit(): Int =
  this - '0'
internal inline fun Char.isBase10Digit(): Boolean =
  this in '0' .. '9'

internal inline fun Char.toBase16Digit(): Int =
  when (this) {
    in '0' .. '9' -> this - '0'
    in 'A' .. 'F' -> this - 'A' + 10
    in 'a' .. 'f' -> this - 'a' + 10
    else          -> throw NumberFormatException()
  }
internal inline fun Char.isBase16Digit(): Boolean =
  this in '0' .. '9' || this in 'A' .. 'F' || this in 'a' .. 'f'
