package com.example.crypto.utils

import java.text.NumberFormat
import java.util.*
import kotlin.math.ln
import kotlin.math.pow

fun Double.coinsPriceConverter(): String {
    return if (this < 1.0) {
        val nf = NumberFormat.getInstance(Locale.US)
        nf.maximumFractionDigits = 6
        nf.format(this)
    } else {
        if (this > 1_000_000.0) {
            val exp = (ln(this) / ln(1000.0)).toInt()
            String.format(Locale.US,"%.1f%c", this / 1000.0.pow(exp.toDouble()), "kMBTPE"[exp - 1])
        } else {
            /** for 1_000_000 < price < 1 **/
            val nf = NumberFormat.getInstance(Locale.US)
            nf.maximumFractionDigits = 2
            nf.format(this)
        }
    }
}

fun Double.coinsPriceConverterWithDollarCharAtStart(): String {
    return if (this < 1.0) {
        val nf = NumberFormat.getInstance(Locale.US)
        nf.maximumFractionDigits = 6
        "$ ${nf.format(this)}"
    } else {
        if (this > 1_000_000.0) {
            val exp = (ln(this) / ln(1000.0)).toInt()
            String.format(Locale.US,"$ %.1f%c", this / 1000.0.pow(exp.toDouble()), "kMBTPE"[exp - 1])
        } else {
            /** for 1_000_000 < price < 1 **/
            val nf = NumberFormat.getInstance(Locale.US)
            nf.maximumFractionDigits = 2
            "$ ${nf.format(this)}"
        }
    }
}