package io.klibs.math.big

fun bigDecimalOf(value: String, mc: MathContext = MathContext.Unlimited): BigDec =
  bigDecimalOf(value.toCharArray(), 0, value.length, mc)

fun bigDecimalOf(value: CharArray, offset: Int = 0, length: Int = value.size, mc: MathContext = MathContext.Unlimited): BigDec {
  try {
    checkFromIndexSize(offset, length, value.size)
  } catch (e: IndexOutOfBoundsException) {
    throw NumberFormatException("bad offset or length arguments for CharArray input")
  }

  var `in` = value
  var len = length
  var offset = offset

  var prec = 0
  var scl = 0
  var rs: Long = 0
  var rb: BigInt? = null

  try {
    var isneg = false

    if (`in`[offset] == '-') {
      isneg = true
      offset++
      len--
    } else if (`in`[offset] == '+') {
      offset++
      len--
    }

    var dot = false
    var exp: Long = 0
    var c: Char
    val isCompact: Boolean = len <= MAX_COMPACT_DIGITS

    var idx = 0
    if (isCompact) {
      while (len > 0) {
        c = `in`[offset]

        if (c == '0') {
          if (prec == 0) prec = 1 else if (rs != 0L) {
            rs *= 10
            ++prec
          }

          if (dot)
            ++scl
        } else if (c >= '1' && c <= '9') {
          val digit = c.code - '0'.code

          if (prec != 1 || rs != 0L)
            ++prec

          rs = rs * 10 + digit

          if (dot)
            ++scl
        } else if (c == '.') {

          if (dot)
            throw NumberFormatException("input contains more than one decimal point")
          dot = true

        } else if (c.isDigit()) {
          val digit = c.digitToIntOrNull() ?: -1
          if (digit == 0) {
            if (prec == 0)
              prec = 1
            else if (rs != 0L) {
              rs *= 10
              ++prec
            }
          } else {
            if (prec != 1 || rs != 0L)
              ++prec

            rs = rs * 10 + digit
          }
          if (dot) ++scl
        } else if ((c == 'e') || (c == 'E')) {
          exp = parseExp(`in`, offset, len)

          if ((exp.toInt()).toLong() != exp)
            throw NumberFormatException("exponent overflow")

          break
        } else {
          throw NumberFormatException("Character $c is neither a decimal digit number, decimal point, nor \"e\" notation exponential mark.")
        }

        offset++
        len--
      }

      if (prec == 0)
        throw NumberFormatException("no digits found")

      if (exp != 0L) {
        scl = adjustScale(scl, exp)
      }

      rs = if (isneg) -rs else rs
      val mcp = mc.precision
      var drop = prec - mcp

      if (mcp > 0 && drop > 0) {
        while (drop > 0) {
          scl = checkScaleNonZero(scl.toLong() - drop)
          rs = divideAndRound(rs, LONG_TEN_POWERS_TABLE[drop], mc.roundingMode)
          prec = longDigitLength(rs)
          drop = prec - mcp
        }
      }

    } else {
      val coeff = CharArray(len)
      while (len > 0) {
        c = `in`[offset]

        if ((c >= '0' && c <= '9') || c.isDigit()) {

          if (c == '0' || (c.digitToIntOrNull() ?: -1) == 0) {
            if (prec == 0) {
              coeff[idx] = c
              prec = 1
            } else if (idx != 0) {
              coeff[idx++] = c
              ++prec
            }
          } else {
            if (prec != 1 || idx != 0) ++prec // prec unchanged if preceded by 0s
            coeff[idx++] = c
          }

          if (dot)
            ++scl

          offset++
          len--
          continue
        }

        if (c == '.') {
          // have dot
          if (dot)
            throw NumberFormatException("input contains more than one decimal point.")

          dot = true
          offset++
          len--

          continue
        }

        if (c != 'e' && c != 'E')
          throw NumberFormatException("input is missing \"e\" notation exponential mark.")

        exp = parseExp(`in`, offset, len)

        if (exp.toInt().toLong() != exp)
          throw NumberFormatException("exponent overflow.")

        break
      }

      if (prec == 0)
        throw NumberFormatException("No digits found.")

      if (exp != 0L) {
        scl = adjustScale(scl, exp)
      }

      rb = bigIntOf(coeff, if (isneg) -1 else 1, prec)
      rs = compactValFor(rb)

      val mcp = mc.precision
      if (mcp > 0 && (prec > mcp)) {

        if (rs == INFLATED) {
          var drop = prec - mcp

          while (drop > 0) {
            scl = checkScaleNonZero(scl.toLong() - drop)
            rb = divideAndRoundByTenPow(rb, drop, mc.roundingMode)
            rs = compactValFor(rb)

            if (rs != INFLATED) {
              prec = longDigitLength(rs)
              break
            }

            prec = bigDigitLength(rb)
            drop = prec - mcp
          }
        }

        if (rs != INFLATED) {
          var drop = prec - mcp

          while (drop > 0) {
            scl = checkScaleNonZero(scl.toLong() - drop)
            rs = divideAndRound(rs, LONG_TEN_POWERS_TABLE[drop], mc.roundingMode)
            prec = longDigitLength(rs)
            drop = prec - mcp
          }
          rb = null
        }
      }
    }
  } catch (e: Exception) {
    throw NumberFormatException(e.message)
  }

  return BigDecImpl(rb, rs, scl, prec)
}
