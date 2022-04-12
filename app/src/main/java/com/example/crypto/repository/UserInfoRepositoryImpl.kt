package com.example.crypto.repository

import com.example.crypto.model.db.CoinsListDataBase
import com.example.crypto.model.settings_db.SettingsUserInfo
import com.example.crypto.repository.interfaces.UserInfoRepository

class UserInfoRepositoryImpl(
    private val coinsListDataBase: CoinsListDataBase
) : UserInfoRepository {

    override suspend fun insertUserInfo(settingsUserInfo: SettingsUserInfo) {
        coinsListDataBase.userInfoDao().insertUserInfo(settingsUserInfo)
    }

    override suspend fun getUserInfo() = coinsListDataBase.userInfoDao().getUserInfo()
}