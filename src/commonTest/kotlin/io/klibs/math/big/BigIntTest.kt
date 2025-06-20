package io.klibs.math.big

import kotlin.test.Test
import kotlin.test.assertEquals

class BigIntTest {
  // ArrayIndexOutOfBoundsException calling BigInt.and(BigInt)
  @Test
  fun issue_1() {
    assertEquals(BigInt.One, BigInt.One and BigInt.One)
  }

  // ArrayIndexOutOfBoundsException calling toString() on 0 value obtained via shr
  @Test
  fun issue_2() {
    assertEquals("0", bigIntOf(1).shr(1).toString())
    assertEquals("0", bigIntOf(1).shl(-7).toString())
  }
}