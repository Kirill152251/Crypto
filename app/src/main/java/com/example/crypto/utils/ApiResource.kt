package com.example.crypto.utils

sealed class ApiResource <out T> {

    data class Success<out T: Any>(val data: T): ApiResource<T>()

    data class Error(val e: Exception): ApiResource<Nothing>()

    companion object {
        const val DEFAULT_ERROR_MESSAGE = "An error has occurred"
    }
}