package com.example.crypto.repository

import com.example.crypto.repository.interfaces.DetailsScreenRepInterface
import com.example.crypto.utils.Resource
import com.github.mikephil.charting.data.Entry

class FakeDetailsScreenRepository: DetailsScreenRepInterface {
    override suspend fun fetchPriceChange(
        coinId: String,
        interval: String
    ): Resource<MutableList<Entry>> {
        val testData = mutableListOf<Entry>()
        testData.add(Entry(10.0f, 12.0f))
        return Resource.success(mutableListOf())
    }

    override suspend fun getMinAndMaxPrice(
        coinId: String,
        interval: String
    ): Resource<List<String>> {
        val testList = listOf("test")
        return Resource.success(testList)
    }
}