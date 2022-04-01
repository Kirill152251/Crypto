package com.example.crypto.repository

import com.example.crypto.model.api.CoinGeckoService
import com.example.crypto.model.constans.NETWORK_PAGE_SIZE
import com.example.crypto.model.constans.QUERY_SORT_BY_MARKET_CAP
import com.example.crypto.model.constans.STARTING_PAGE_INDEX
import com.example.crypto.model.db.CoinsListDataBase
import com.example.crypto.repository.interfaces.SplashScreenRepository
import java.lang.Exception

class SplashScreenRepositoryImpl(
    private val service: CoinGeckoService,
    private val coinsListDataBase: CoinsListDataBase
) : SplashScreenRepository {

    override suspend fun fetchingAndCachingInitialCoins(): Boolean {
        return try {
            val initialCoins = service.getCoinsSortedByMarketCap(
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
}