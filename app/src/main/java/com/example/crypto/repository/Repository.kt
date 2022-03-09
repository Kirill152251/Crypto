package com.example.crypto.repository

import android.util.Log
import androidx.paging.*
import com.example.crypto.R
import com.example.crypto.model.api.CoinGeckoService
import com.example.crypto.model.api.responses.coinsList.Coin
import com.example.crypto.model.constans.*
import com.example.crypto.model.db.CoinsListDataBase
import com.example.crypto.utils.Resource
import com.example.crypto.utils.converter
import com.example.crypto.views.fragments.mainScreen.*
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.flow.Flow
import java.text.NumberFormat


const val STARTING_PAGE_INDEX = 1
const val NETWORK_PAGE_SIZE = 20


class Repository(
    private val service: CoinGeckoService,
    private val coinsListDataBase: CoinsListDataBase
) {
    private lateinit var priceDataFromApi: List<List<Double>>

    suspend fun fetchPriceChange(coinId: String, interval: String): Resource<MutableList<Entry>> {
        //var priceDataFromApi = emptyList<List<Double>>()
        try {
            when(interval) {
                LABEL_DAY -> priceDataFromApi = service.getPriceChangePerDay(coinId = coinId).prices
                LABEL_WEEK -> priceDataFromApi = service.getPriceChangePerWeek(coinId = coinId).prices
                LABEL_MONTH -> priceDataFromApi = service.getPriceChangePerMonth(coinId = coinId).prices
                LABEL_YEAR -> priceDataFromApi = service.getPriceChangePerYear(coinId = coinId).prices
                LABEL_ALL_TIME -> priceDataFromApi = service.getPriceChangeForAllTime(coinId = coinId).prices
            }
            val priceData = mutableListOf<Entry>()
            for (i in priceDataFromApi.indices) {
                priceData.add(Entry(i.toFloat(), priceDataFromApi[i][1].toFloat()))
            }
            return Resource.success(priceData)
        } catch (e: Exception) {
            return Resource.error("An error has occurred", null)
        }
    }

    suspend fun getMinAndMaxPrice(coinId: String, interval: String): Resource<List<String>> {
        try {
            when(interval) {
                LABEL_DAY -> priceDataFromApi = service.getPriceChangePerDay(coinId = coinId).prices
                LABEL_WEEK -> priceDataFromApi = service.getPriceChangePerWeek(coinId = coinId).prices
                LABEL_MONTH -> priceDataFromApi = service.getPriceChangePerMonth(coinId = coinId).prices
                LABEL_YEAR -> priceDataFromApi = service.getPriceChangePerYear(coinId = coinId).prices
                LABEL_ALL_TIME -> priceDataFromApi = service.getPriceChangeForAllTime(coinId = coinId).prices
            }
            val minAndMaxPrice = mutableListOf<String>()
            val prices = mutableListOf<Double>()
            for (i in priceDataFromApi.indices) {
                prices.add(priceDataFromApi[i][1])
            }
            val min = prices.minOrNull() ?: 0.0
            val max = prices.maxOrNull() ?: 0.0
            if (min < 1 || max < 1) {
                //for price < 1
                val nf = NumberFormat.getInstance()
                nf.maximumFractionDigits = 7
                minAndMaxPrice.add(nf.format(min))
                minAndMaxPrice.add(nf.format(max))
                return Resource.success(minAndMaxPrice)
            } else {
                return if (min > 10_000_000) {
                    //for price > 10_000_000
                    minAndMaxPrice.add(converter(min))
                    minAndMaxPrice.add(converter(max))
                    Resource.success(minAndMaxPrice)
                } else {
                    //for 10_000_000 < price < 0
                    val nf = NumberFormat.getInstance()
                    nf.maximumFractionDigits = 1
                    minAndMaxPrice.add(nf.format(min))
                    minAndMaxPrice.add(nf.format(max))
                    Resource.success(minAndMaxPrice)
                }
            }
        } catch (e: Exception) {
            return Resource.error("An error has occurred", null)
        }
    }

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
            Log.i("error", e.message.toString())
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