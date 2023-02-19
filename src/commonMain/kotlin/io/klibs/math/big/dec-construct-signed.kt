package io.klibs.math.big

// Static Factory Methods
fun bigDecimalOf(unscaledVal: Long, scale: Int): BigDec {
  if (scale == 0) {
    return bigDecimalOf(unscaledVal)
  } else if (unscaledVal == 0L) {
    return zeroValueOf(scale)
  } else
    return BigDecImpl(
      if (unscaledVal == INFLATED) INFLATED_BIGINT else null,
      unscaledVal,
      scale,
      0
    )
}

fun bigDecimalOf(value: Long): BigDec {
  if (value == 0L)
    return BigDec.Zero
  else if (value == 1L)
    return BigDec.One
  else if (value == 10L)
    return BigDec.Ten
  else if (value != INFLATED)
    return BigDecImpl(null, value, 0, 0)
  else
    return BigDecImpl(INFLATED_BIGINT, value, 0, 0)
}
