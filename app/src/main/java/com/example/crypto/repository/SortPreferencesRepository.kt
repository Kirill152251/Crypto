package com.example.crypto.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.crypto.model.constans.PREFERENCES_NAME
import com.example.crypto.model.constans.QUERY_SORT_BY_MARKET_CAP
import kotlinx.coroutines.flow.first
import java.util.concurrent.Flow

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

class SortPreferencesRepository(private val context: Context) {

    companion object{
        val ORDER = stringPreferencesKey("ORDER")
    }

    suspend fun saveOrder(sortBy: String){
        context.dataStore.edit { preferences ->
            preferences[ORDER] = sortBy
        }
    }

    suspend fun getOrder(): String {
        val preferences = context.dataStore.data.first()
        return preferences[ORDER] ?: QUERY_SORT_BY_MARKET_CAP
    }
}