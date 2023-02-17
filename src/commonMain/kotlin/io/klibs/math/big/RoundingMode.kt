package io.klibs.math.big

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.jvm.JvmStatic

@JsExport
@OptIn(ExperimentalJsExport::class)
enum class RoundingMode {
  Up,
  Down,
  Ceiling,
  Floor,
  HalfUp,
  HalfDown,
  HalfEven,
  Unnecessary,
  ;

  override fun toString(): String {
    return ordinal.toString()
  }

  companion object {
    @JvmStatic
    fun fromString(value: String): RoundingMode =
      try {
        values()[value.toInt()]
      } catch (e: Throwable) {
        throw IllegalArgumentException("unrecognized RoundingMode value: $value")
      }
  }
}