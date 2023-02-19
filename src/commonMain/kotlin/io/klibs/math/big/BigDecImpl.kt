package io.klibs.math.big

internal class BigDecImpl : BigDec {
  private val intVal: BigInt?

  override val scale: Int

  override val precision: Int

  private val intCompact: Long

  constructor(intVal: BigInt?, value: Long, scale: Int, precision: Int) {
    this.intVal = intVal
    this.intCompact = value
    this.precision = precision
    this.scale = scale
  }
}