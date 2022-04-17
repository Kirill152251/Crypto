package com.example.crypto.repository

import androidx.paging.*
import com.example.crypto.model.api.CoinGeckoService
import com.example.crypto.model.api.responses.coins_list.Coin
import com.example.crypto.model.constans.NETWORK_PAGE_SIZE
import com.example.crypto.model.constans.PagingSourceType.*
import com.example.crypto.model.db.AppDataBase
import com.example.crypto.repository.interfaces.MainScreenRepository
import com.example.crypto.views.fragments.main_screen.*
import kotlinx.coroutines.flow.Flow


class MainScreenRepositoryImpl(
    private val service: CoinGeckoService,
    private val appDataBase: AppDataBase
) : MainScreenRepository {

    override fun getCoinsByPrice(): Flow<PagingData<Coin>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                initialLoadSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CoinsPagingSource(
                    service, appDataBase,
                    SORT_BY_PRICE
                )
            }
        ).flow
    }


    override fun getCoinsByCap(): Flow<PagingData<Coin>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                initialLoadSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CoinsPagingSource(
                    service, appDataBase,
                    SORT_BY_MARKET_CAP
                )
            }
        ).flow
    }

    override fun getCoinsByVol(): Flow<PagingData<Coin>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                initialLoadSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CoinsPagingSource(
                    service, appDataBase,
                    SORT_BY_VOLATILITY
                )
            }
        ).flow
    }

    override fun getCoinsFromDB(): Flow<PagingData<Coin>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                initialLoadSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CoinsPagingSource(
                    service, appDataBase,
                    FROM_DB
                )
            }
        ).flow
    }
}