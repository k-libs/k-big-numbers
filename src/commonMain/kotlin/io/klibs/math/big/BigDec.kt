package io.klibs.math.big


sealed interface BigDec {
  val scale: Int
  val precision: Int

  operator fun plus(rhs: BigDec): BigDec
  fun plus(rhs: BigDec, mc: MathContext): BigDec

  operator fun minus(rhs: BigDec): BigDec
  fun minus(rhs: BigDec, mc: MathContext): BigDec

  operator fun times(rhs: BigDec): BigDec
  fun times(rhs: BigDec, mc: MathContext): BigDec

  operator fun div(rhs: BigDec): BigDec
  fun div(rhs: BigDec, mc: MathContext): BigDec
  fun div(rhs: BigDec, rm: RoundingMode): BigDec

  operator fun rem(rhs: BigDec): BigDec
  fun rem(rhs: BigDec, mc: MathContext): BigDec

  fun divideAndRemainder(rhs: BigDec): BigDecDivAndRemResult
  fun divideAndRemainder(rhs: BigDec, mc: MathContext): BigDecDivAndRemResult

  fun sqrt(mc: MathContext): BigDec

  infix fun pow(rhs: Int): BigDec
  fun pow(rhs: Int, mc: MathContext): BigDec

  fun abs(): BigDec
  fun abs(mc: MathContext): BigDec

  operator fun unaryMinus(): BigDec
  fun negate(mc: MathContext): BigDec

  companion object {
    val Zero: BigDec = BigDecImpl(BigInt.Zero, 0, 0, 1)
    val One: BigDec = BigDecImpl(BigInt.One, 1, 0, 1)
    val Ten: BigDec = BigDecImpl(BigInt.Ten, 10, 0, 2)
  }
}
