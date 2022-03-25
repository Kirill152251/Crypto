package com.example.crypto.model.settingsDB

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [SettingsUserInfo::class],
    version = 6,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class UserInfoDataBase: RoomDatabase() {

    abstract fun userInfoDao(): UserInfoDao
}