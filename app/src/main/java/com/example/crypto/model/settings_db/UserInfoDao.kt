package com.example.crypto.model.settings_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserInfo(userInfo: SettingsUserInfo)

    @Query("select * from user_info")
    suspend fun getUserInfo(): SettingsUserInfo
}