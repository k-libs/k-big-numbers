package io.klibs.math.big

import kotlin.test.Test
import kotlin.test.assertEquals

class MutableBigIntImplTest {

  @Test
  fun fitsByte_1() {
    data class Test(val input: String, val expect: Boolean)

    val tests = arrayOf(
      Test("0", true),
      Test("127", true),
      Test("-128", true),
      Test("128", false),
      Test("-129", false),
    )

    for (test in tests)
      assertEquals(test.expect, test.input.toBigInt().fitsByte(), "failed on input: ${test.input}")
  }

  @Test
  fun fitsShort_1() {
    data class Test(val input: String, val expect: Boolean)

    val tests = arrayOf(
      Test("0", true),
      Test("32767", true),
      Test("-32768", true),
      Test("32768", false),
      Test("-32769", false),
    )

    for (test in tests)
      assertEquals(test.expect, test.input.toBigInt().fitsShort(), "failed on input: ${test.input}")
  }

  @Test
  fun fitsInt_1() {
    data class Test(val input: String, val expect: Boolean)

    val tests = arrayOf(
      Test("0", true),
      Test("2147483647", true),
      Test("-2147483648", true),
      Test("2147483648", false),
      Test("-2147483649", false),
    )

    for (test in tests)
      assertEquals(test.expect, test.input.toBigInt().fitsInt(), "failed on input: ${test.input}")
  }

  @Test
  fun fitsLong_1() {
    data class Test(val input: String, val expect: Boolean)

    val tests = arrayOf(
      Test("0", true),
      Test("9223372036854775807", true),
      Test("-9223372036854775808", true),
      Test("9223372036854775808", false),
      Test("-9223372036854775809", false),
    )

    for (test in tests)
      assertEquals(test.expect, test.input.toBigInt().fitsLong(), "failed on input: ${test.input}")
  }

  @Test
  fun plusAssign_positives_1() {

  }
}