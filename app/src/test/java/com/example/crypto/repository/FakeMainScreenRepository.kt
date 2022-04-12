package com.example.crypto.repository

import androidx.paging.PagingData
import com.example.crypto.model.api.responses.coins_list.Coin
import com.example.crypto.repository.interfaces.MainScreenRepository
import com.example.crypto.utils.testFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeMainScreenRepository: MainScreenRepository {

    override fun getCoinsByPrice(): Flow<PagingData<Coin>> {
        return flow { PagingData.empty<Coin>() }
    }

    override fun getCoinsByCap(): Flow<PagingData<Coin>> {
        return flow { PagingData.empty<Coin>() }
    }

    override fun getCoinsByVol(): Flow<PagingData<Coin>> {
        return flow { PagingData.empty<Coin>() }
    }

    override fun getCoinsFromDB(): Flow<PagingData<Coin>> {
        return flow { PagingData.empty<Coin>() }
    }
}