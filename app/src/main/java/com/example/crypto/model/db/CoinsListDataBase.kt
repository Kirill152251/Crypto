package com.example.crypto.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.crypto.model.api.responses.coins_list.Coin

@Database(
    entities = [Coin::class],
    version = 1,
    exportSchema = false
)
abstract class CoinsListDataBase: RoomDatabase() {

    abstract fun coinsListDao(): CoinsListDao
}