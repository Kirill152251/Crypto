package com.example.crypto.repository

import android.util.Log
import com.example.crypto.model.api.CoinGeckoService
import com.example.crypto.model.constans.*
import com.example.crypto.repository.interfaces.DetailsScreenRepInterface
import com.example.crypto.utils.Resource
import com.example.crypto.utils.myConverter
import com.github.mikephil.charting.data.Entry

class DetailsScreenRepositoryImp(private val service: CoinGeckoService) :
    DetailsScreenRepInterface {

    override suspend fun fetchPriceChange(
        coinId: String,
        interval: String
    ): Resource<MutableList<Entry>> {
        try {
            val priceDataFromApi = when (interval) {
                LABEL_DAY ->
                    service.getPriceChangePerDay(coinId = coinId).prices
                LABEL_WEEK ->
                    service.getPriceChangePerWeek(coinId = coinId).prices
                LABEL_MONTH ->
                    service.getPriceChangePerMonth(coinId = coinId).prices
                LABEL_YEAR ->
                    service.getPriceChangePerYear(coinId = coinId).prices
                else ->
                    service.getPriceChangeForAllTime(coinId = coinId).prices
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

    override suspend fun getMinAndMaxPrice(
        coinId: String,
        interval: String
    ): Resource<List<String>> {
        try {
            val priceDataFromApi = when (interval) {
                LABEL_DAY -> service.getPriceChangePerDay(coinId = coinId).prices
                LABEL_WEEK ->
                    service.getPriceChangePerWeek(coinId = coinId).prices
                LABEL_MONTH ->
                    service.getPriceChangePerMonth(coinId = coinId).prices
                LABEL_YEAR ->
                    service.getPriceChangePerYear(coinId = coinId).prices
                else ->
                    service.getPriceChangeForAllTime(coinId = coinId).prices
            }
            val minAndMaxPrice = mutableListOf<String>()
            val prices = mutableListOf<Double>()
            for (i in priceDataFromApi.indices) {
                prices.add(priceDataFromApi[i][1])
            }
            val min = prices.minOrNull() ?: 0.0
            val max = prices.maxOrNull() ?: 0.0
            minAndMaxPrice.add(myConverter(min))
            minAndMaxPrice.add(myConverter(max))
            return Resource.success(minAndMaxPrice)
        } catch (e: Exception) {
            return Resource.error("An error has occurred", null)
        }
    }
}