package io.klibs.math.big

internal const val MAX_MAG_LENGTH = Int.MAX_VALUE / Int.SIZE_BITS + 1

internal fun checkRange(mag: IntArray) {
  if (mag.size > MAX_MAG_LENGTH || mag.size == MAX_MAG_LENGTH && mag[0] < 0)
    reportOverflow()
}

internal inline fun abs(a: Long): Long = if (a < 0) -a else a