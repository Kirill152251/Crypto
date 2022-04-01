package com.example.crypto.repository.interfaces

import com.example.crypto.utils.ApiResource
import com.github.mikephil.charting.data.Entry

interface DetailsScreenRepository {

    suspend fun fetchPriceChange(coinId: String, interval: String): ApiResource<MutableList<Entry>>

    suspend fun getMinAndMaxPrice(coinId: String, interval: String): ApiResource<List<String>>
}