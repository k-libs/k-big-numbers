package io.klibs.math

import io.klibs.collections.UByteDeque

internal inline fun UByteDeque.getOrZero(index: Int) = if (index < size) get(index) else UBYTE_0