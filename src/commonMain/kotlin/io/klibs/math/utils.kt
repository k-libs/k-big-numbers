package io.klibs.math

internal const val BYTE_0: Byte = 0
internal const val BYTE_1: Byte = 1
internal const val BYTE_2: Byte = 2
internal const val BYTE_3: Byte = 3
internal const val BYTE_4: Byte = 4
internal const val BYTE_5: Byte = 5
internal const val BYTE_6: Byte = 6
internal const val BYTE_7: Byte = 7
internal const val BYTE_8: Byte = 8
internal const val BYTE_9: Byte = 9

internal const val B_ASCII_ZERO: Byte = 48
internal const val U_ASCII_ZERO: UByte = 48u

internal inline fun min(a: Int, b: Int): Int = if (a < b) a else b
internal inline fun max(a: Int, b: Int): Int = if (a > b) a else b

internal fun Char.isBase10Digit(): Boolean = this in '0' .. '9'

internal fun Byte.decStringWidth() =
  when {
    this < -99 -> 4
    this < -9  -> 3
    this < 0   -> 2
    this < 10  -> 1
    this < 100 -> 2
    else       -> 3
  }

internal fun Short.decStringWidth() =
  when {
    this < 0 -> when {
      this < -9999 -> 6
      this < -999  -> 5
      this < -99   -> 4
      this < -9    -> 3
      else         -> 2
    }

    else     -> when {
      this > 9999 -> 5
      this > 999  -> 4
      this > 99   -> 3
      this > 9    -> 2
      else        -> 1
    }
  }

internal fun Int.decStringWidth() =
  when {
    this < 0 -> when {
      this < -999999999 -> 11
      this < -99999999  -> 10
      this < -9999999   -> 9
      this < -999999    -> 8
      this < -99999     -> 7
      this < -9999      -> 6
      this < -999       -> 5
      this < -99        -> 4
      this < -9         -> 3
      else              -> 2
    }

    else     -> when {
      this > 999999999 -> 10
      this > 99999999  -> 9
      this > 9999999   -> 8
      this > 999999    -> 7
      this > 99999     -> 6
      this > 9999      -> 5
      this > 999       -> 4
      this > 99        -> 3
      this > 9         -> 2
      else             -> 1
    }
  }

internal fun Long.decStringWidth() =
  when {
    this < 0L -> when {
      this < -999999999999999999L -> 20
      this < -99999999999999999L  -> 19
      this < -9999999999999999L   -> 18
      this < -999999999999999L    -> 17
      this < -99999999999999L     -> 16
      this < -9999999999999L      -> 15
      this < -999999999999L       -> 14
      this < -99999999999L        -> 13
      this < -9999999999L         -> 12
      this < -999999999L          -> 11
      this < -99999999L           -> 10
      this < -9999999L            -> 9
      this < -999999L             -> 8
      this < -99999L              -> 7
      this < -9999L               -> 6
      this < -999L                -> 5
      this < -99L                 -> 4
      this < -9L                  -> 3
      else                        -> 2
    }

    else      -> when {
      this < 10L                  -> 1
      this < 100L                 -> 2
      this < 1000L                -> 3
      this < 10000L               -> 4
      this < 100000L              -> 5
      this < 1000000L             -> 6
      this < 10000000L            -> 7
      this < 100000000L           -> 8
      this < 1000000000L          -> 9
      this < 10000000000L         -> 10
      this < 100000000000L        -> 11
      this < 1000000000000L       -> 12
      this < 10000000000000L      -> 13
      this < 100000000000000L     -> 14
      this < 1000000000000000L    -> 15
      this < 10000000000000000L   -> 16
      this < 100000000000000000L  -> 17
      this < 1000000000000000000L -> 18
      else                        -> 19
    }
  }

internal fun UByte.decStringWidth() =
  when {
    this > 99u -> 3
    this > 9u  -> 2
    else       -> 1
  }

internal fun UShort.decStringWidth() =
  when {
    this > 9999u -> 5
    this > 999u  -> 4
    this > 99u   -> 3
    this > 9u    -> 2
    else         -> 1
  }

internal fun UInt.decStringWidth() =
  when {
    this > 999999999u -> 10
    this > 99999999u  -> 9
    this > 9999999u   -> 8
    this > 999999u    -> 7
    this > 99999u     -> 6
    this > 9999u      -> 5
    this > 999u       -> 4
    this > 99u        -> 3
    this > 9u         -> 2
    else              -> 1
  }

internal fun ULong.decStringWidth() =
  when {
    this > 9999999999999999999uL -> 20
    this > 999999999999999999uL  -> 19
    this > 99999999999999999uL   -> 18
    this > 9999999999999999uL    -> 17
    this > 999999999999999uL     -> 16
    this > 99999999999999uL      -> 15
    this > 9999999999999uL       -> 14
    this > 999999999999uL        -> 13
    this > 99999999999uL         -> 12
    this > 9999999999uL          -> 11
    this > 999999999uL           -> 10
    this > 99999999uL            -> 9
    this > 9999999uL             -> 8
    this > 999999uL              -> 7
    this > 99999uL               -> 6
    this > 9999uL                -> 5
    this > 999uL                 -> 4
    this > 99uL                  -> 3
    this > 9uL                   -> 2
    else                         -> 1
  }
