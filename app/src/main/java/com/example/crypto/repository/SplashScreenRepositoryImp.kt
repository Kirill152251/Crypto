package com.example.crypto.repository

import android.util.Log
import com.example.crypto.model.api.CoinGeckoService
import com.example.crypto.model.constans.QUERY_SORT_BY_MARKET_CAP
import com.example.crypto.model.db.CoinsListDataBase
import com.example.crypto.repository.interfaces.SplashScreenRepInterface
import java.lang.Exception

class SplashScreenRepositoryImp(
    private val service: CoinGeckoService,
    private val coinsListDataBase: CoinsListDataBase
) : SplashScreenRepInterface {

    override suspend fun fetchingAndCachingInitialCoins() {
        try {
            val initialCoins = service.getTwentyCoinSortedByMarketCap(
                STARTING_PAGE_INDEX,
                NETWORK_PAGE_SIZE,
                QUERY_SORT_BY_MARKET_CAP
            )
            coinsListDataBase.coinsListDao().insertCoins(initialCoins)
        } catch (e: Exception) {
            //TODO
        }
    }
}