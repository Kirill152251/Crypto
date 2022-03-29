package com.example.crypto.repository.interfaces

import androidx.paging.PagingData
import com.example.crypto.model.api.responses.coins_list.Coin
import kotlinx.coroutines.flow.Flow

interface MainScreenRepInterface {

    fun getCoinsByPrice(): Flow<PagingData<Coin>>

    fun getCoinsByCap(): Flow<PagingData<Coin>>

    fun getCoinsByVol(): Flow<PagingData<Coin>>

    fun getCoinsFromDB(): Flow<PagingData<Coin>>
}