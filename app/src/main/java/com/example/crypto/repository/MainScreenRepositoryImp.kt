package com.example.crypto.repository

import androidx.paging.*
import com.example.crypto.model.api.CoinGeckoService
import com.example.crypto.model.api.responses.coinsList.Coin
import com.example.crypto.model.db.CoinsListDataBase
import com.example.crypto.repository.interfaces.MainScreenRepInterface
import com.example.crypto.views.fragments.mainScreen.*
import kotlinx.coroutines.flow.Flow


const val STARTING_PAGE_INDEX = 1
const val NETWORK_PAGE_SIZE = 20


class MainScreenRepositoryImp(
    private val service: CoinGeckoService,
    private val coinsListDataBase: CoinsListDataBase
) : MainScreenRepInterface {

    override fun getCoinsByPrice(): Flow<PagingData<Coin>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                initialLoadSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CoinsPagingSourceByPrice(service) }
        ).flow
    }

    override fun getCoinsByCap(): Flow<PagingData<Coin>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                initialLoadSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CoinsPagingSourceByCap(service) }
        ).flow
    }

    override fun getCoinsByVol(): Flow<PagingData<Coin>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                initialLoadSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CoinsPagingSourceByVol(service) }
        ).flow
    }

    override fun getCoinsFromDB(): Flow<PagingData<Coin>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                initialLoadSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CoinsPagingSourceFromDb(coinsListDataBase) }
        ).flow
    }
}