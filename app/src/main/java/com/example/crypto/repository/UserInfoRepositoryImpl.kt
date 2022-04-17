package com.example.crypto.repository

import com.example.crypto.model.db.AppDataBase
import com.example.crypto.model.settings_db.SettingsUserInfo
import com.example.crypto.repository.interfaces.UserInfoRepository

class UserInfoRepositoryImpl(
    private val appDataBase: AppDataBase
) : UserInfoRepository {

    override suspend fun insertUserInfo(settingsUserInfo: SettingsUserInfo) {
        appDataBase.userInfoDao().insertUserInfo(settingsUserInfo)
    }

    override suspend fun getUserInfo() = appDataBase.userInfoDao().getUserInfo()
}