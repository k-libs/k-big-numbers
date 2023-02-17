package io.klibs.math.big

sealed interface BigDec {
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
}