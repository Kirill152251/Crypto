package com.example.crypto.repository

import android.util.Log
import com.example.crypto.model.api.CoinGeckoService
import com.example.crypto.model.api.responses.coins_list.mapToEntity
import com.example.crypto.model.constans.NETWORK_PAGE_SIZE
import com.example.crypto.model.constans.QUERY_SORT_BY_MARKET_CAP
import com.example.crypto.model.constans.STARTING_PAGE_INDEX
import com.example.crypto.model.db.AppDataBase
import com.example.crypto.repository.interfaces.SplashScreenRepository
import retrofit2.HttpException

class SplashScreenRepositoryImpl(
    private val service: CoinGeckoService,
    private val appDataBase: AppDataBase
) : SplashScreenRepository {

    override suspend fun fetchingAndCachingInitialCoins(): Boolean {
        return try {
            val initialCoins = service.getCoinsSortedByMarketCap(
                STARTING_PAGE_INDEX,
                NETWORK_PAGE_SIZE,
                QUERY_SORT_BY_MARKET_CAP
            )
            appDataBase.coinsListDao().insertCoins(initialCoins.map { it.mapToEntity() })
            true
        } catch (e: HttpException) {
            Log.i("error", e.toString())
            false
        }
    }
}