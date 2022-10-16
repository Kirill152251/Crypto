package com.example.crypto.repository

import android.graphics.Bitmap
import com.example.crypto.model.internal_storage.StorageCommunicator
import com.example.crypto.model.settings_db.SettingsUserInfo
import com.example.crypto.model.settings_db.UserInfoDao
import com.example.crypto.repository.interfaces.UserInfoRepository
import com.example.crypto.utils.Avatar

class UserInfoRepositoryImpl(
    private val dao: UserInfoDao,
    private val internalStorageCommunicator: StorageCommunicator
) : UserInfoRepository {

    override suspend fun insertUserInfo(settingsUserInfo: SettingsUserInfo) {
        dao.insertUserInfo(settingsUserInfo)
    }

    override suspend fun getUserInfo() = dao.getUserInfo()

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