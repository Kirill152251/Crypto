package com.example.crypto.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CoinsListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoins(coins: List<CoinEntity>)

    @Query("select * from coins")
    suspend fun getCoinsList(): List<CoinEntity>

    @Query("delete from coins")
    suspend fun clearDb()
}