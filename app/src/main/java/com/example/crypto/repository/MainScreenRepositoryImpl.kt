package com.example.crypto.repository

import androidx.paging.*
import com.example.crypto.model.api.CoinGeckoService
import com.example.crypto.model.api.responses.coins_list.Coin
import com.example.crypto.model.constans.NETWORK_PAGE_SIZE
import com.example.crypto.model.db.CoinsListDataBase
import com.example.crypto.repository.interfaces.MainScreenRepository
import com.example.crypto.views.fragments.main_screen.*
import kotlinx.coroutines.flow.Flow


class MainScreenRepositoryImpl(
    private val service: CoinGeckoService,
    private val coinsListDataBase: CoinsListDataBase
) : MainScreenRepository {

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