package com.example.crypto.repository.interfaces

import com.example.crypto.utils.Resource
import com.github.mikephil.charting.data.Entry

interface DetailsScreenRepInterface {

    suspend fun fetchPriceChange(coinId: String, interval: String): Resource<MutableList<Entry>>

    suspend fun getMinAndMaxPrice(coinId: String, interval: String): Resource<List<String>>
}