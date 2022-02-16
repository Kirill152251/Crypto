package com.example.crypto.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.crypto.model.api.responses.coinsList.Coin

@Database(
    entities = [Coin::class],
    version = 1,
    exportSchema = false
)
abstract class CoinsListDataBase: RoomDatabase() {

    abstract fun coinsListDao(): CoinsListDao
    companion object {
        @Volatile private var instance: CoinsListDataBase? = null

        fun getDatabase(context: Context): CoinsListDataBase =
            instance?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it }}

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, CoinsListDataBase::class.java, "Coins.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}