package io.klibs.math

class UByteDigits(initialCapacity: Int) {
  @OptIn(ExperimentalUnsignedTypes::class)
  private var buffer = UByteArray(initialCapacity)

  var size = 0
    private set

  val capacity
    get() = buffer.size

  inline val lastIndex
    get() = size - 1

  fun pushHead(b: UByte)
  fun popHead(): UByte
  fun peekHead(): UByte

  fun pushTail(b: UByte)
  fun popTail(): UByte
  fun peekTail(): UByte

  fun ensureCapacity(minCapacity: Int)

  fun trim()

  operator fun set(i: Int, b: UByte)

  operator fun get(i: Int): UByte
}