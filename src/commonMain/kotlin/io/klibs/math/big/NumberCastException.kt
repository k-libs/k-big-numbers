package io.klibs.math.big

class NumberCastException : Throwable {
  constructor() : super()
  constructor(msg: String) : super(msg)
  constructor(cause: Throwable) : super(cause)
  constructor(msg: String, cause: Throwable) : super(msg, cause)
}