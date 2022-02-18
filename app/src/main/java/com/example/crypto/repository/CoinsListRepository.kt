package com.example.crypto.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.crypto.model.api.CoinGeckoService
import com.example.crypto.model.api.responses.coinsList.Coin
import com.example.crypto.model.db.CoinsListDataBase
import com.example.crypto.views.fragments.mainScreen.CoinsMediator
import com.example.crypto.views.fragments.mainScreen.CoinsPagingSource
import kotlinx.coroutines.flow.Flow


const val STARTING_PAGE_INDEX = 1
const val NETWORK_PAGE_SIZE = 20


class CoinsListRepository(
    private val service: CoinGeckoService,
    private val coinsListDataBase: CoinsListDataBase
) {
    @ExperimentalPagingApi
    fun getCoinsListFlowFromDb(): Flow<PagingData<Coin>> {
        val pagingSourceFactory = { coinsListDataBase.coinsListDao().getCoins() }

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                initialLoadSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = CoinsMediator(service, coinsListDataBase),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}