package com.example.crypto.model.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.crypto.model.api.responses.coinsList.Coin

@Dao
interface CoinsListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoins(coins: List<Coin>)

    @Query("select * from coins_table")
    fun getCoins(): PagingSource<Int, Coin>

    @Query("select * from coins_table")
    fun getCoinsList(): List<Coin>

    @Query("delete from coins_table")
    suspend fun clearDb()

    @Query("select * from coins_table order by marketCapRank desc limit 20")
    suspend fun getFirstTwentyCoinsFromDb(): List<Coin>
}