package io.klibs.math.big

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@JsExport
@OptIn(ExperimentalJsExport::class)
data class BigDecDivAndRemResult(val quotient: BigDec, val remainder: BigDec)