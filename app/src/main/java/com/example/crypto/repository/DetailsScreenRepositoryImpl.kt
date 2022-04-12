package com.example.crypto.repository

import com.example.crypto.model.api.CoinGeckoService
import com.example.crypto.model.constans.*
import com.example.crypto.repository.interfaces.DetailsScreenRepository
import com.example.crypto.utils.ApiResourceForPriceCharts
import com.example.crypto.utils.coinsPriceConverter
import com.github.mikephil.charting.data.Entry

class DetailsScreenRepositoryImpl(private val service: CoinGeckoService) :
    DetailsScreenRepository {
    override suspend fun fetchPriceChange(
        coinId: String,
        interval: String
    ): ApiResourceForPriceCharts<MutableList<Entry>> {
        try {
            val priceDataFromApi = when (interval) {
                LABEL_DAY -> service.getPriceChangePerDay(coinId = coinId).prices
                LABEL_WEEK -> service.getPriceChangePerWeek(coinId = coinId).prices
                LABEL_MONTH -> service.getPriceChangePerMonth(coinId = coinId).prices
                LABEL_YEAR -> service.getPriceChangePerYear(coinId = coinId).prices
                else -> service.getPriceChangeForAllTime(coinId = coinId).prices
            }
            val prices = mutableListOf<Double>()
            for (i in priceDataFromApi.indices) {
                prices.add(priceDataFromApi[i][1])
            }
            val min = prices.minOrNull() ?: 0.0
            val max = prices.maxOrNull() ?: 0.0

            val priceData = mutableListOf<Entry>()
            for (i in priceDataFromApi.indices) {
                priceData.add(Entry(i.toFloat(), priceDataFromApi[i][1].toFloat()))
            }
            return ApiResourceForPriceCharts.Success(priceData, min.coinsPriceConverter(), max.coinsPriceConverter())
        } catch (e: Exception) {
            return ApiResourceForPriceCharts.Error(e)
        }
    }
}