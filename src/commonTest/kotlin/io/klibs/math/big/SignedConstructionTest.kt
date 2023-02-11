package io.klibs.math.big

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SignedConstructionTest {
  @Test
  fun bigIntOf_byte_1() {
    data class Test(val input: Byte, val expect: String)

    val tests = arrayOf(
      Test(0, "0"),
      Test(1, "1"),
      Test(-1, "-1"),
      Test(127, "127"),
      Test(-128, "-128"),
    )

    for (test in tests)
      assertEquals(test.expect, bigIntOf(test.input).toPlainString())
  }

  @Test
  fun bigIntOf_short_1() {
    data class Test(val input: Short, val expect: String)

    val tests = arrayOf(
      Test(0, "0"),
      Test(1, "1"),
      Test(-1, "-1"),
      Test(32767, "32767"),
      Test(-32768, "-32768"),
    )

    for (test in tests)
      assertEquals(test.expect, bigIntOf(test.input).toPlainString())
  }

  @Test
  fun bigIntOf_int_1() {
    data class Test(val input: Int, val expect: String)

    val tests = arrayOf(
      Test(0, "0"),
      Test(1, "1"),
      Test(-1, "-1"),
      Test(Int.MAX_VALUE, "2147483647"),
      Test(Int.MIN_VALUE, "-2147483648"),
    )

    for (test in tests)
      assertEquals(test.expect, bigIntOf(test.input).toPlainString())
  }

  @Test
  fun bigIntOf_long_1() {
    data class Test(val input: Long, val expect: String)

    val tests = arrayOf(
      Test(0, "0"),
      Test(1, "1"),
      Test(-1, "-1"),
      Test(Long.MAX_VALUE, "9223372036854775807"),
      Test(Long.MIN_VALUE, "-9223372036854775808"),
    )

    for (test in tests)
      assertEquals(test.expect, bigIntOf(test.input).toPlainString())
  }

  @Test
  fun bigIntOf_string_base10_1() {
    data class Test(val input: String, val expect: String)

    val tests = arrayOf(
      Test("0", "0"),
      Test("1", "1"),
      Test("-1", "-1"),
      Test("127", "127"),
      Test("-128", "-128"),
      Test("32767", "32767"),
      Test("-32768", "-32768"),
    )

    for (test in tests)
      assertEquals(test.expect, bigIntOf(test.input).toPlainString())
  }

  @Test
  fun bigIntOf_string_base10_2() {
    val tests = arrayOf("a")

    for (test in tests)
      assertFailsWith<NumberFormatException> { bigIntOf(test) }
  }
}