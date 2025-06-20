package io.klibs.math.big

import kotlin.test.Test
import kotlin.test.assertEquals

class BigIntTest {
  @Test
  fun issue_2_shr_to_zero_causes_array_out_of_bounds() {
    assertEquals("0", bigIntOf(1).shr(1).toString())
    assertEquals("0", bigIntOf(1).shl(-7).toString())
  }
}