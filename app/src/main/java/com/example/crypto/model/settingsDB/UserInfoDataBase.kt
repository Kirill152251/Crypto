package com.example.crypto.model.settingsDB

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [SettingsUserInfo::class],
    version = 2,
    exportSchema = false
)
abstract class UserInfoDataBase: RoomDatabase() {

    abstract fun userInfoDao(): UserInfoDao
}