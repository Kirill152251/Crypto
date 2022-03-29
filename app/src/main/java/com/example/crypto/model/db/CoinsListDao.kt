package com.example.crypto.model.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.crypto.model.api.responses.coins_list.Coin

@Dao
interface CoinsListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoins(coins: List<Coin>)

    @Query("select * from coins")
    fun getCoins(): PagingSource<Int, Coin>

    @Query("select * from coins")
    suspend fun getCoinsList(): List<Coin>

    @Query("delete from coins")
    suspend fun clearDb()
}