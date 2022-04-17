package com.example.crypto.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.crypto.model.settings_db.SettingsUserInfo
import com.example.crypto.model.settings_db.UserInfoDao

@Database(
    entities = [CoinEntity::class, SettingsUserInfo::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase: RoomDatabase() {

    abstract fun coinsListDao(): CoinsListDao
    abstract fun userInfoDao(): UserInfoDao
}