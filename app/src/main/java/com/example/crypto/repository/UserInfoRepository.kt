package com.example.crypto.repository

import android.graphics.Bitmap
import com.example.crypto.model.settingsDB.SettingsUserInfo
import com.example.crypto.model.settingsDB.UserInfoDataBase

class UserInfoRepository(
    private val userInfoDataBase: UserInfoDataBase
) {

    suspend fun insertUserInfo(settingsUserInfo: SettingsUserInfo) {
        userInfoDataBase.userInfoDao().insertUserInfo(settingsUserInfo)
    }

    suspend fun updateProfilePicture(picture: Bitmap) {
        userInfoDataBase.userInfoDao().updateProfilePicture(picture)
    }

    fun getUserInfo() = userInfoDataBase.userInfoDao().getUserInfo()
}