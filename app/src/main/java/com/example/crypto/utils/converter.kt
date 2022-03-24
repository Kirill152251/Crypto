package com.example.crypto.utils

import java.text.NumberFormat
import java.util.*
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.roundToLong

fun myConverter(price: Double): String {
    return if (price < 1.0) {
        val nf = NumberFormat.getInstance(Locale.US)
        nf.maximumFractionDigits = 6
        nf.format(price)
    } else {
        if (price > 1_000_000.0) {
            val exp = (ln(price) / ln(1000.0)).toInt()
            String.format(Locale.US,"%.1f%c", price / 1000.0.pow(exp.toDouble()), "kMBTPE"[exp - 1])
        } else {
            /** for 1_000_000 < price < 1 **/
            val nf = NumberFormat.getInstance(Locale.US)
            nf.maximumFractionDigits = 2
            nf.format(price)
        }
    }
}
