package com.example.crypto.repository.interfaces

import com.example.crypto.utils.ApiResourceForPriceCharts
import com.github.mikephil.charting.data.Entry

interface DetailsScreenRepository {

    suspend fun fetchPriceChange(coinId: String, interval: String): ApiResourceForPriceCharts<MutableList<Entry>>

}