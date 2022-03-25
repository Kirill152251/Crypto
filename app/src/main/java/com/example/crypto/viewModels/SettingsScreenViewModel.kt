package com.example.crypto.viewModels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crypto.model.settingsDB.SettingsUserInfo
import com.example.crypto.repository.UserInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsScreenViewModel(
    private val repository: UserInfoRepository
) : ViewModel() {

    fun insertUserInfo(settingsUserInfo: SettingsUserInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertUserInfo(settingsUserInfo)
        }
    }

    fun updateProfilePicture(picture: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateProfilePicture(picture)
        }
    }

    fun getUserInfo() = repository.getUserInfo()
}