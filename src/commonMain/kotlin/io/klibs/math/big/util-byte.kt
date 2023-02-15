package io.klibs.math.big

internal fun abs(byte: Byte) = if (byte < 0) (-byte).toByte() else byte