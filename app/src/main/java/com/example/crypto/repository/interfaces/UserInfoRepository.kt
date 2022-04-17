package com.example.crypto.repository.interfaces

import com.example.crypto.model.settings_db.SettingsUserInfo

interface UserInfoRepository {

    suspend fun insertUserInfo(settingsUserInfo: SettingsUserInfo)

    suspend fun getUserInfo(): SettingsUserInfo
}