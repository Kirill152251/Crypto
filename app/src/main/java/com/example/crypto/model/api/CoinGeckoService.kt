package com.example.crypto.model.api

import com.example.crypto.model.api.responses.coinsList.CoinListResponse
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=20&page=1&sparkline=false

interface CoinGeckoService {

    @GET("markets")
    suspend fun getTwentyCoins (
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ) : CoinListResponse
}