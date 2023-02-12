package io.klibs.math.big

enum class BigIntRadix {
//  Two,
//  Eight,

  Ten {
    override val value = 10u

    override val bitsPerDigit = 4

    override val digitsPerInt = 9

    override fun charToDigit(c: Char) =
      if (c !in '0' .. '9')
        throw NumberFormatException("invalid base 10 digit: $c")
      else
        (c - '0').toUInt()
  },
//  Sixteen,
  ;

  abstract val value: UInt
  abstract val bitsPerDigit: Int
  abstract val digitsPerInt: Int
  abstract fun charToDigit(c: Char): UInt
}