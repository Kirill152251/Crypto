package com.example.crypto.model.db

import androidx.paging.PagingData
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

    @Query("SELECT * FROM COINS_TABLE ORDER BY currentPrice DESC")
    fun getCoinsSortedByPrice(): PagingSource<Int, Coin>

    @Query("SELECT * FROM COINS_TABLE ORDER BY abs(volatility) DESC")
    fun getCoinsSortedByVolatility(): PagingSource<Int, Coin>

    @Query("SELECT * FROM COINS_TABLE ORDER BY marketCapRank ASC")
    fun getCoinsSortedByMarketCap(): PagingSource<Int, Coin>
}