package com.example.crypto.repository

import android.graphics.Bitmap
import com.example.crypto.model.settings_db.SettingsUserInfo
import com.example.crypto.model.settings_db.UserInfoDataBase
import com.example.crypto.repository.interfaces.UserInfoRepository

class UserInfoRepositoryImpl(
    private val userInfoDataBase: UserInfoDataBase
): UserInfoRepository {

    override suspend fun insertUserInfo(settingsUserInfo: SettingsUserInfo) {
        userInfoDataBase.userInfoDao().insertUserInfo(settingsUserInfo)
    }

    override suspend fun updateProfilePicture(picture: Bitmap) {
        userInfoDataBase.userInfoDao().updateProfilePicture(picture)
    }

    override fun getUserInfo() = userInfoDataBase.userInfoDao().getUserInfo()
}