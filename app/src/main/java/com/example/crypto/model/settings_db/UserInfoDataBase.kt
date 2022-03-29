package com.example.crypto.model.settings_db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [SettingsUserInfo::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class UserInfoDataBase: RoomDatabase() {

    abstract fun userInfoDao(): UserInfoDao
}