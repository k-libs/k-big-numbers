package io.klibs.math

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFailsWith

class BigIntImplTest {

  // region toByte()

  @Test
  fun toByte_1() {
    data class test(val expect: Byte, val input: String)

    val tests = arrayOf(
      test(0, "0"),
      test(1, "1"),
      test(127, "127"),
      test(-1, "-1"),
      test(-128, "-128")
    )

    for (test in tests) {
      assertEquals(test.expect, bigIntOf(test.input).toByte())
    }
  }

  @Test
  fun toByte_2() {
    val tests = arrayOf("-129", "128")

    for (test in tests)
      assertFailsWith<NumberCastException> { bigIntOf(test).toByte() }
  }

  // endregion toByte()

  // region toShort()

  @Test
  fun toShort_1() {
    data class Test(val expect: Short, val input: String)

    val tests = arrayOf(
      Test(0, "0"),
      Test(1, "1"),
      Test(32767, "32767"),
      Test(-1, "-1"),
      Test(-32768, "-32768")
    )

    for (test in tests) {
      assertEquals(test.expect, bigIntOf(test.input).toShort())
    }
  }

  @Test
  fun toShort_2() {
    val tests = arrayOf("-32769", "32768")

    for (test in tests)
      assertFailsWith<NumberCastException> { bigIntOf(test).toShort() }
  }

  // endregion toShort()

  // region toInt()

  @Test
  fun toInt_1() {
    data class Test(val expect: Int, val input: String)

    val tests = arrayOf(
      Test(0, "0"),
      Test(1, "1"),
      Test(2147483647, "2147483647"),
      Test(-1, "-1"),
      Test(-2147483648, "-2147483648")
    )

    for (test in tests)
      assertEquals(test.expect, bigIntOf(test.input).toInt())
  }

  @Test
  fun toInt_2() {
    val tests = arrayOf("-2147483649", "2147483648")

    for (test in tests)
      assertFailsWith<NumberCastException> { bigIntOf(test).toInt() }
  }

  // endregion toInt()

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

  // region minus(Byte)

  @Test
  fun minus_byte_1() {
    val tgt = bigIntOf("10")

    println((tgt - 100).toPlainString())
  }

  // endregion minus(Byte)
}