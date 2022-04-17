package com.example.crypto.utils

sealed class ApiResourceForPriceCharts<out T> {

    data class Success<out T : Any>(
        val data: T,
        val minPrice: String,
        val maxPrice: String
    ) : ApiResourceForPriceCharts<T>()

    data class Error(val e: String) : ApiResourceForPriceCharts<Nothing>()

    companion object {
        const val DEFAULT_ERROR_MESSAGE = "An error has occurred"
    }
}