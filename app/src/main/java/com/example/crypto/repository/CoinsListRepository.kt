package com.example.crypto.repository

import androidx.paging.*
import com.example.crypto.model.api.CoinGeckoService
import com.example.crypto.model.api.responses.coinsList.Coin
import com.example.crypto.model.constans.SORT_BY_MARKET_CAP
import com.example.crypto.model.constans.SORT_BY_PRICE
import com.example.crypto.model.constans.SORT_BY_VOLATILITY
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
    fun getCoinsListFlowFromDb(order: String): Flow<PagingData<Coin>> {

        val pagingSourceFactory = when(order) {
            SORT_BY_PRICE -> { { coinsListDataBase.coinsListDao().getCoinsSortedByPrice() } }
            SORT_BY_VOLATILITY -> { { coinsListDataBase.coinsListDao().getCoinsSortedByVolatility() } }
            else -> { { coinsListDataBase.coinsListDao().getCoinsSortedByMarketCap() } }
        }


        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                initialLoadSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = CoinsMediator(service, coinsListDataBase),
            pagingSourceFactory = { coinsListDataBase.coinsListDao().getCoins() }
        ).flow
    }
}