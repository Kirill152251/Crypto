package com.example.crypto.repository.interfaces

interface SortPreferencesRepInterface {

    suspend fun saveOrder(sortBy: String)

    suspend fun getOrder(): String
}