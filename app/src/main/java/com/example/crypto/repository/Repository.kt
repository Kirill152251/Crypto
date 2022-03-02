package com.example.crypto.repository

import androidx.paging.*
import com.example.crypto.model.api.CoinGeckoService
import com.example.crypto.model.api.responses.coinsList.Coin
import com.example.crypto.model.constans.QUERY_SORT_BY_MARKET_CAP
import com.example.crypto.model.db.CoinsListDataBase
import com.example.crypto.views.fragments.mainScreen.*
import kotlinx.coroutines.flow.Flow


const val STARTING_PAGE_INDEX = 1
const val NETWORK_PAGE_SIZE = 20


class Repository(
    private val service: CoinGeckoService,
    private val coinsListDataBase: CoinsListDataBase
) {

    suspend fun isFetchingAndCachingInitialCoinsDone(): Boolean {
        return try {
            val initialCoins = service.getTwentyCoinSortedByMarketCap(
                STARTING_PAGE_INDEX,
                NETWORK_PAGE_SIZE,
                QUERY_SORT_BY_MARKET_CAP
            )
            coinsListDataBase.coinsListDao().insertCoins(initialCoins)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getCoinsByPrice(): Flow<PagingData<Coin>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                initialLoadSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CoinsPagingSourceByPrice(service, coinsListDataBase) }
        ).flow
    }

    fun getCoinsByCap(): Flow<PagingData<Coin>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                initialLoadSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CoinsPagingSourceByCap(service, coinsListDataBase) }
        ).flow
    }

    fun getCoinsByVol(): Flow<PagingData<Coin>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                initialLoadSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CoinsPagingSourceByVol(service, coinsListDataBase) }
        ).flow
    }
}