package io.klibs.math

import io.klibs.collections.ByteDeque

internal inline fun ByteDeque.getOrZero(index: Int) = if (index < size) get(index) else BYTE_0