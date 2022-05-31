package com.example.crypto.repository.interfaces

import android.graphics.Bitmap
import com.example.crypto.model.settings_db.SettingsUserInfo
import com.example.crypto.utils.Avatar

interface UserInfoRepository {

    suspend fun insertUserInfo(settingsUserInfo: SettingsUserInfo)

    suspend fun getUserInfo(): SettingsUserInfo

    fun deleteAvatar()

    suspend fun getAvatar(): List<Avatar>

    fun saveAvatar(bitmap: Bitmap): Boolean
}