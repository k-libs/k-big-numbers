package io.klibs.math.big


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
