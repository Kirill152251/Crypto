package com.example.crypto.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crypto.model.settingsDB.SettingsUserInfo
import com.example.crypto.repository.UserInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsScreenViewModel(
    private val repository: UserInfoRepository
): ViewModel() {

    fun insertUserInfo(settingsUserInfo: SettingsUserInfo) {
        viewModelScope.launch {
            repository.insertUserInfo(settingsUserInfo)
        }
    }

    suspend fun getUserInfo() = withContext(Dispatchers.IO) {
        repository.getUserInfo()
    }
}