package io.klibs.math.big


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
