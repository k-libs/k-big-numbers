package io.klibs.math.big

enum class BigIntRadix {
//  Two,
//  Eight,

  Ten {
    override val value = 10u

    override val bitsPerDigit = 4

    override val digitsPerInt = 9

    override val digitsPerLong = 18

    override val longRadix by lazy { bigIntOf(0xde0b6b3a7640000L) }

    override fun charToDigit(c: Char) =
      if (c !in '0' .. '9')
        throw NumberFormatException("invalid base 10 digit: $c")
      else
        (c - '0').toUInt()
  },
//  Sixteen,
  ;

  internal abstract val value: UInt
  internal abstract val bitsPerDigit: Int
  internal abstract val digitsPerInt: Int
  internal abstract val digitsPerLong: Int
  internal abstract val longRadix: BigInt
  internal abstract fun charToDigit(c: Char): UInt
}