package com.example.crypto.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.crypto.model.api.responses.coinsList.Coin

@Database(
    entities = [Coin::class],
    version = 4,
    exportSchema = false
)
abstract class CoinsListDataBase: RoomDatabase() {

    abstract fun coinsListDao(): CoinsListDao

}