package io.klibs.math

import kotlin.test.Test
import kotlin.test.assertEquals

class BigIntImplTest {

  // region plus(Byte)

  @Test
  fun plus_byte_1() {
    val add: Byte = 0
    val tgt = bigIntOf("1234") + add

    assertEquals("1234", tgt.toPlainString())
  }

  @Test
  fun plus_byte_2() {
    val add: Byte = 1
    val tgt = bigIntOf("1234") + add

    assertEquals("1235", tgt.toPlainString())
  }

  @Test
  fun plus_byte_3() {
    val add: Byte = 127
    val tgt = bigIntOf("1234") + add

    assertEquals("1361", tgt.toPlainString())
  }

//  @Test
  fun plus_byte_4() {
    val add: Byte = -1
  }

//  @Test
  fun plus_byte_5() {
    val add: Byte = -128
  }

  // endregion plus(Byte)

  // region plus(Short)

  @Test
  fun plus_short_1() {
    val add: Short = 0
    val tgt = bigIntOf("1234") + add

    assertEquals("1234", tgt.toPlainString())
  }

  @Test
  fun plus_short_2() {
    val add: Short = 1
    val tgt = bigIntOf("1234") + add

    assertEquals("1235", tgt.toPlainString())
  }

  @Test
  fun plus_short_3() {
    val add: Short = 32767
    val tgt = bigIntOf("1234") + add

    assertEquals("34001", tgt.toPlainString())
  }

  // endregion plus(Short)

  // region plus(Int)

  @Test
  fun plus_int_1() {
    val add = 0
    val tgt = bigIntOf("1234") + add

    assertEquals("1234", tgt.toPlainString())
  }

  @Test
  fun plus_int_2() {
    val add = 1
    val tgt = bigIntOf("1234") + add

    assertEquals("1235", tgt.toPlainString())
  }

  @Test
  fun plus_int_3() {
    val add = 2147483647
    val tgt = bigIntOf("1234") + add

    assertEquals("2147484881", tgt.toPlainString())
  }

  // endregion plus(Int)

  // region plus(Long)

  @Test
  fun plus_long_1() {
    val add = 0L
    val tgt = bigIntOf("1234") + add

    assertEquals("1234", tgt.toPlainString())
  }

  @Test
  fun plus_long_2() {
    val add = 1L
    val tgt = bigIntOf("1234") + add

    assertEquals("1235", tgt.toPlainString())
  }

  @Test
  fun plus_long_3() {
    val add = 9223372036854775807L
    val tgt = bigIntOf("1234") + add

    assertEquals("9223372036854777041", tgt.toPlainString())
  }

  // endregion plus(Long)
}