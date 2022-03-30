package com.example.crypto.repository.interfaces

interface SortPreferencesRepository {

    suspend fun saveOrder(sortBy: String)

    suspend fun getOrder(): String
}