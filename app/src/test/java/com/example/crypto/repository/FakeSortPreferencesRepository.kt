package com.example.crypto.repository

import com.example.crypto.repository.interfaces.SortPreferencesRepInterface

class FakeSortPreferencesRepository : SortPreferencesRepInterface {
    override suspend fun saveOrder(sortBy: String) {
    }

    override suspend fun getOrder(): String {
        return "test"
    }
}