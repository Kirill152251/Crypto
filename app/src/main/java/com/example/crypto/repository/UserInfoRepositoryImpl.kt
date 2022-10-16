package com.example.crypto.repository

import android.graphics.Bitmap
import com.example.crypto.model.internal_storage.InternalStorageCommunicator
import com.example.crypto.model.db.AppDataBase
import com.example.crypto.model.settings_db.SettingsUserInfo
import com.example.crypto.repository.interfaces.UserInfoRepository
import com.example.crypto.utils.Avatar

class UserInfoRepositoryImpl(
    private val appDataBase: AppDataBase,
    private val internalStorageCommunicator: InternalStorageCommunicator
) : UserInfoRepository {

    override suspend fun insertUserInfo(settingsUserInfo: SettingsUserInfo) {
        appDataBase.userInfoDao().insertUserInfo(settingsUserInfo)
    }

    override suspend fun getUserInfo() = appDataBase.userInfoDao().getUserInfo()

    override fun deleteAvatar() {
        internalStorageCommunicator.deleteContentFromStorage()
    }

    override suspend fun getAvatar(): List<Avatar> {
        return internalStorageCommunicator.getContentFromStorage()
    }

    override suspend fun saveAvatar(bitmap: Bitmap): Boolean {
        return internalStorageCommunicator.saveContentToStorage(bitmap)
    }
}