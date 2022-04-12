package com.example.crypto.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.crypto.model.settings_db.SettingsUserInfo
import com.example.crypto.model.settings_db.UserInfoDao

@Database(
    entities = [CoinEntity::class, SettingsUserInfo::class],
    version = 5,
    exportSchema = false
)
abstract class CoinsListDataBase: RoomDatabase() {

    abstract fun coinsListDao(): CoinsListDao
    abstract fun userInfoDao(): UserInfoDao
}