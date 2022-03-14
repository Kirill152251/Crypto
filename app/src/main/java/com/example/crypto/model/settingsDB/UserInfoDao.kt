package com.example.crypto.model.settingsDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserInfo(userInfo: SettingsUserInfo)

    @Query("select * from user_info")
    fun getUserInfo(): SettingsUserInfo
}