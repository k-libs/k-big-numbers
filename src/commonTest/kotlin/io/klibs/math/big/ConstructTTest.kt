package io.klibs.math.big

import kotlin.test.Test
import kotlin.test.assertEquals

class ConstructTTest {

  @Test
  fun bigIntOf_base10_zeros() {
    data class Test(val input: String, val expect: String)

    val tests = arrayOf(
      Test("0", "0"),
      Test("-0", "0"),
      Test("+0", "0"),
      Test("00000000000000000000000000000000000000000000000000000000000", "0"),
      Test("-00000000000000000000000000000000000000000000000000000000000", "0"),
      Test("+00000000000000000000000000000000000000000000000000000000000", "0"),
    )

    for (test in tests)
      assertEquals(test.expect, bigIntOf(test.input).toString())
  }

  @Test
  fun bigIntOf_base10_1() {
    data class Test(val input: String, val expect: String)

    val tests = arrayOf(
      Test("-1", "-1"),
      Test("1", "1"),
      Test("+1", "1"),
      Test("-128", "-128"),
      Test("127", "127"),
      Test("255", "255"),
      Test("-32768", "-32768"),
      Test("32767", "32767"),
      Test("65535", "65535"),
      Test("-2147483648", "-2147483648"),
      Test("2147483647", "2147483647"),
      Test("4294967295", "4294967295"),
      Test("-9223372036854775808", "-9223372036854775808"),
      Test("9223372036854775807", "9223372036854775807"),
      Test("18446744073709551615", "18446744073709551615"),
      Test("92233720368547758079223372036854775807", "92233720368547758079223372036854775807"),
      Test("8507059173023461586441102613027942441933008747414884640827396907784232501249", "8507059173023461586441102613027942441933008747414884640827396907784232501249")
    )

    for (test in tests)
      assertEquals(test.expect, bigIntOf(test.input).toString())
  }
}