package com.example.crypto.repository

import com.example.crypto.model.CoinsResponseResult
import com.example.crypto.model.api.CoinGeckoService
import com.example.crypto.model.api.responses.coinsList.Coin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class CoinsListRepository(private val service: CoinGeckoService) {

    private val inMemoryCache = mutableListOf<Coin>()
    private val responseResults = MutableSharedFlow<CoinsResponseResult>(replay = 1)
    private var lastRequestedPage = STARTING_PAGE_INDEX
    private var isRequestInProgress = false


    suspend fun getCoinsListResultStream(order: String): Flow<CoinsResponseResult> {
        lastRequestedPage = 1
        inMemoryCache.clear()
        requestAndSaveData(order)

        return responseResults
    }

    suspend fun requestMore(order: String) {
        if (isRequestInProgress) return
        val successful = requestAndSaveData(order)
        if (successful) {
            lastRequestedPage++
        }
    }

    private suspend fun requestAndSaveData(order: String): Boolean {
        isRequestInProgress = true
        var successful = false
        try {
            val response = service.getTwentyCoins(lastRequestedPage, NETWORK_PAGE_SIZE, order).toList()
            inMemoryCache.addAll(response)
            responseResults.emit(CoinsResponseResult.Success(inMemoryCache))
            successful = true
        } catch (exception: IOException) {
            responseResults.emit(CoinsResponseResult.Error(exception))
        } catch (exception: HttpException) {
            responseResults.emit(CoinsResponseResult.Error(exception))
        }
        isRequestInProgress = false
        return successful
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }
}