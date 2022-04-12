package com.example.crypto.model.settings_db

import android.graphics.Bitmap
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserInfo(userInfo: SettingsUserInfo)

    @Query("select * from user_info")
    fun getUserInfo(): SettingsUserInfo
}