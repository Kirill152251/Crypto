package com.example.crypto.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.crypto.model.constans.PREFERENCES_NAME
import com.example.crypto.model.constans.QUERY_SORT_BY_MARKET_CAP
import com.example.crypto.repository.interfaces.SortPreferencesRepository
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

class SortPreferencesRepositoryImpl(private val context: Context) : SortPreferencesRepository {

    companion object{
        val ORDER = stringPreferencesKey("ORDER")
    }

    override suspend fun saveOrder(sortBy: String){
        context.dataStore.edit { preferences ->
            preferences[ORDER] = sortBy
        }
    }

    override suspend fun getOrder(): String {
        val preferences = context.dataStore.data.first()
        return preferences[ORDER] ?: QUERY_SORT_BY_MARKET_CAP
    }
}