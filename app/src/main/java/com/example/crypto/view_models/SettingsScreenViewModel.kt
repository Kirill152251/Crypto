package com.example.crypto.view_models

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crypto.model.settings_db.SettingsUserInfo
import com.example.crypto.repository.UserInfoRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsScreenViewModel(
    private val repository: UserInfoRepositoryImpl
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