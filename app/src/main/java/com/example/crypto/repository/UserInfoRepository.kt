package com.example.crypto.repository

import com.example.crypto.model.settingsDB.SettingsUserInfo
import com.example.crypto.model.settingsDB.UserInfoDataBase

class UserInfoRepository(
    private val userInfoDataBase: UserInfoDataBase
) {

    suspend fun insertUserInfo(settingsUserInfo: SettingsUserInfo) {
        userInfoDataBase.userInfoDao().insertUserInfo(settingsUserInfo)
    }


   suspend fun getUserInfo(): SettingsUserInfo {
        return userInfoDataBase.userInfoDao().getUserInfo()
    }
}