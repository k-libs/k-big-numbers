package io.klibs.math

import kotlin.test.Test
import kotlin.test.assertEquals
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

  // region times()

  @Test
  fun times_byte_1() {
    data class Test(val a: Byte, val b: Byte, val expect: String)

    val tests = arrayOf(
      Test(0, 0, "0"),             // 0
      Test(0, 1, "0"),             // 1
      Test(1, 0, "0"),             // 2
      Test(1, 1, "1"),             // 3
      Test(1, -1, "-1"),           // 4
      Test(-1, 1, "-1"),           // 5
      Test(-1, -1, "1"),           // 6
      Test(1, 127, "127"),         // 7
      Test(1, -128, "-128"),        // 8
      Test(127, -128, "-16256")    // 9
    )

    tests.forEachIndexed { i, test ->
      assertEquals(test.expect, (bigIntOf(test.a) * test.b).toPlainString(), "Test $i failed.")
    }
  }

  @Test
  fun times_short_1() {
    data class Test(val a: Short, val b: Short, val expect: String)

    val tests = arrayOf(
      Test(0, 0, "0"),                   // 0
      Test(0, 1, "0"),                   // 1
      Test(1, 0, "0"),                   // 2
      Test(1, 1, "1"),                   // 3
      Test(1, -1, "-1"),                 // 4
      Test(-1, 1, "-1"),                 // 5
      Test(-1, -1, "1"),                 // 6
      Test(1, 32767, "32767"),           // 7
      Test(1, -32768, "-32768"),         // 8
      Test(32767, -32768, "-1073709056") // 9
    )

    tests.forEachIndexed { i, test ->
      assertEquals(test.expect, (bigIntOf(test.a) * test.b).toPlainString(), "Test $i failed.")
    }
  }

  @Test
  fun times_int_1() {
    data class Test(val a: Int, val b: Int, val expect: String)

    val tests = arrayOf(
      Test(0, 0, "0"),
      Test(0, 1, "0"),
      Test(1, 0, "0"),
      Test(1, 1, "1"),
      Test(1, -1, "-1"),
      Test(-1, 1, "-1"),
      Test(-1, -1, "1"),
      Test(1, 2147483647, "2147483647"),
      Test(1, -2147483648, "-2147483648"),
      Test(2147483647, -2147483648, "-4611686016279904256")
    )

    tests.forEachIndexed { i, test ->
      assertEquals(test.expect, (bigIntOf(test.a) * test.b).toPlainString(), "Test $i failed.")
    }
  }

  @Test
  fun times_long_1() {
    data class Test(val a: Long, val b: Long, val expect: String)

    val tests = arrayOf(
      Test(0, 0, "0"),
      Test(0, 1, "0"),
      Test(1, 0, "0"),
      Test(1, 1, "1"),
      Test(1, -1, "-1"),
      Test(-1, 1, "-1"),
      Test(-1, -1, "1"),
      Test(1, Long.MAX_VALUE, "9223372036854775807"),
      Test(1, Long.MIN_VALUE, "-9223372036854775808"),
      Test(Long.MAX_VALUE, Long.MIN_VALUE, "-85070591730234615856620279821087277056")
    )

    tests.forEachIndexed { i, test ->
      assertEquals(test.expect, (bigIntOf(test.a) * test.b).toPlainString(), "Test $i failed.")
    }
  }

  @Test
  fun times_ubyte_1() {
    data class Test(val a: UByte, val b: UByte, val expect: String)

    val tests = arrayOf(
      Test(0u, 0u, "0"),
      Test(0u, 1u, "0"),
      Test(1u, 0u, "0"),
      Test(1u, 1u, "1"),
      Test(1u, 255u, "255"),
      Test(255u, 255u, "65025")
    )

    tests.forEachIndexed { i, test ->
      assertEquals(test.expect, (bigIntOf(test.a) * test.b).toPlainString(), "Test $i failed.")
    }
  }

  @Test
  fun times_ushort_1() {
    data class Test(val a: UShort, val b: UShort, val expect: String)

    val tests = arrayOf(
      Test(0u, 0u, "0"),
      Test(0u, 1u, "0"),
      Test(1u, 0u, "0"),
      Test(1u, 1u, "1"),
      Test(1u, 65535u, "65535"),
      Test(65535u, 65535u, "4294836225")
    )

    tests.forEachIndexed { i, test ->
      assertEquals(test.expect, (bigIntOf(test.a) * test.b).toPlainString(), "Test $i failed.")
    }
  }

  @Test
  fun times_uint_1() {
    data class Test(val a: UInt, val b: UInt, val expect: String)

    val tests = arrayOf(
      Test(0u, 0u, "0"),
      Test(0u, 1u, "0"),
      Test(1u, 0u, "0"),
      Test(1u, 1u, "1"),
      Test(1u, 4294967295u, "4294967295"),
      Test(4294967295u, 4294967295u, "18446744065119617025")
    )

    tests.forEachIndexed { i, test ->
      assertEquals(test.expect, (bigIntOf(test.a) * test.b).toPlainString(), "Test $i failed.")
    }
  }

  @Test
  fun times_ulong_1() {
    data class Test(val a: ULong, val b: ULong, val expect: String)

    val tests = arrayOf(
      Test(0u, 0u, "0"),
      Test(0u, 1u, "0"),
      Test(1u, 0u, "0"),
      Test(1u, 1u, "1"),
      Test(1u, 18446744073709551615u, "18446744073709551615"),
      Test(18446744073709551615u, 18446744073709551615u, "340282366920938463426481119284349108225")
    )

    tests.forEachIndexed { i, test ->
      assertEquals(test.expect, (bigIntOf(test.a) * test.b).toPlainString(), "Test $i failed.")
    }
  }

  // endregion times()
}