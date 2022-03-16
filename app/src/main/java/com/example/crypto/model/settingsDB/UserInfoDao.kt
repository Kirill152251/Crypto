package com.example.crypto.model.settingsDB

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

//    @Query("select * from user_info")
//    suspend fun getUserInfo(): SettingsUserInfo

    @Query("select * from user_info")
    fun getUserInfo(): Flow<SettingsUserInfo>

    @Query("update user_info set profilePicture = :picture where id = 1")
    suspend fun updateProfilePicture(picture: Bitmap)
}