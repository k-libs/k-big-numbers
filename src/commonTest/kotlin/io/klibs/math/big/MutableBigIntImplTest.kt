package io.klibs.math.big

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

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
  fun fitsUByte_1() {
    data class Test(val input: String, val expect: Boolean)

    val tests = arrayOf(
      Test("0", true),
      Test("255", true),
      Test("-1", false),
      Test("256", false),
    )

    for (test in tests)
      assertEquals(test.expect, test.input.toBigInt().fitsUByte(), "failed on input: ${test.input}")
  }

  @Test
  fun toUByte_1() {
    data class Test(val input: String, val expect: UByte)

    val tests = arrayOf(
      Test("0", 0u),
      Test("1", 1u),
      Test("255", 255u),
    )

    for (test in tests)
      assertEquals(test.expect, test.input.toBigInt().toUByte())
  }

  @Test
  fun toUByte_2() {
    val tests = arrayOf("-1", "256")

    for (test in tests)
      assertFailsWith<NumberCastException> { test.toBigInt().toUByte() }
  }

  @Test
  fun fitsUShort_1() {
    data class Test(val input: String, val expect: Boolean)

    val tests = arrayOf(
      Test("0", true),
      Test("65535", true),
      Test("-1", false),
      Test("65536", false),
    )

    for (test in tests)
      assertEquals(test.expect, test.input.toBigInt().fitsUShort(), "failed on input: ${test.input}")
  }

  @Test
  fun toUShort_1() {
    data class Test(val input: String, val expect: UShort)

    val tests = arrayOf(
      Test("0", 0u),
      Test("1", 1u),
      Test("65535", 65535u),
    )

    for (test in tests)
      assertEquals(test.expect, test.input.toBigInt().toUShort())
  }

  @Test
  fun toUShort_2() {
    val tests = arrayOf("-1", "65536")

    for (test in tests)
      assertFailsWith<NumberCastException> { test.toBigInt().toUShort() }
  }

  @Test
  fun fitsUInt_1() {
    data class Test(val input: String, val expect: Boolean)

    val tests = arrayOf(
      Test("0", true),
      Test("4294967295", true),
      Test("-1", false),
      Test("4294967296", false),
    )

    for (test in tests)
      assertEquals(test.expect, test.input.toBigInt().fitsUInt(), "failed on input: ${test.input}")
  }

  @Test
  fun toUInt_1() {
    data class Test(val input: String, val expect: UInt)

    val tests = arrayOf(
      Test("0", 0u),
      Test("1", 1u),
      Test("4294967295", 4294967295u),
    )

    for (test in tests)
      assertEquals(test.expect, test.input.toBigInt().toUInt())
  }

  @Test
  fun toUInt_2() {
    val tests = arrayOf("-1", "4294967296")

    for (test in tests)
      assertFailsWith<NumberCastException> { test.toBigInt().toUInt() }
  }

  @Test
  fun fitsULong_1() {
    data class Test(val input: String, val expect: Boolean)

    val tests = arrayOf(
      Test("0", true),
      Test("18446744073709551615", true),
      Test("-1", false),
      Test("18446744073709551616", false),
    )

    for (test in tests)
      assertEquals(test.expect, test.input.toBigInt().fitsULong(), "failed on input: ${test.input}")
  }

  @Test
  fun toULong_1() {
    data class Test(val input: String, val expect: ULong)

    val tests = arrayOf(
      Test("0", 0u),
      Test("1", 1u),
      Test("18446744073709551615", 18446744073709551615u),
    )

    for (test in tests)
      assertEquals(test.expect, test.input.toBigInt().toULong())
  }

  @Test
  fun toULong_2() {
    val tests = arrayOf("-1", "18446744073709551616")

    for (test in tests)
      assertFailsWith<NumberCastException> { test.toBigInt().toULong() }
  }

}