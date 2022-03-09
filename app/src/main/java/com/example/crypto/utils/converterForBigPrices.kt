package com.example.crypto.utils

import kotlin.math.ln
import kotlin.math.pow

fun converter(count: Double): String {
    if (count < 1000) return "$ $count"
    val exp = (ln(count) / ln(1000.0)).toInt()
    return String.format("$ %.1f%c", count / 1000.0.pow(exp.toDouble()), "kMBTPE"[exp - 1])
}