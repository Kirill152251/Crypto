package com.example.crypto.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.crypto.model.api.responses.coins_list.Coin
import com.example.crypto.model.settings_db.SettingsUserInfo
import com.example.crypto.model.settings_db.UserInfoDao

@Database(
    entities = [Coin::class, SettingsUserInfo::class],
    version = 2,
    exportSchema = false
)
abstract class CoinsListDataBase: RoomDatabase() {

    abstract fun coinsListDao(): CoinsListDao
    abstract fun userInfoDao(): UserInfoDao
}