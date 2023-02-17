package io.klibs.math.big

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

private const val MIN_DIGITS = 0

@JsExport
@OptIn(ExperimentalJsExport::class)
class MathContext(
  internal val precision: Int,
  internal val roundingMode: RoundingMode,
) {
  init {
    if (precision < MIN_DIGITS)
      throw IllegalArgumentException("precision ($precision) < $MIN_DIGITS")
  }

  override fun toString() = "$precision:$roundingMode"

  override fun equals(other: Any?) = (this === other)
    || (other is MathContext && precision == other.precision && roundingMode == other.roundingMode)

  override fun hashCode() = 31 * precision + roundingMode.hashCode()

  companion object {
    val Unlimited = MathContext(0, RoundingMode.HalfUp)
    val Decimal32 = MathContext(7, RoundingMode.HalfEven)
    val Decimal64 = MathContext(16, RoundingMode.HalfEven)
    val Decimal128 = MathContext(34, RoundingMode.HalfEven)
  }
}