package com.example.crypto.repository.interfaces

import android.graphics.Bitmap
import com.example.crypto.model.settings_db.SettingsUserInfo
import kotlinx.coroutines.flow.Flow

interface UserInfoRepository {

    suspend fun insertUserInfo(settingsUserInfo: SettingsUserInfo)

    suspend fun updateProfilePicture(picture: Bitmap)

    fun getUserInfo(): Flow<SettingsUserInfo>
}