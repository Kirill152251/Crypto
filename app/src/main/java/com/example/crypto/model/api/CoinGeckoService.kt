package com.example.crypto.model.api

import com.example.crypto.model.api.responses.coinsList.CoinListResponse
import com.example.crypto.model.api.responses.priceChange.PriceChangePerDay
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=20&page=1&sparkline=false
//https://api.coingecko.com/api/v3/coins/bitcoin/market_chart?vs_currency=usd&days=1

interface CoinGeckoService {

    @GET("{coin_id}/market_chart")
    suspend fun getPriceChangeForAllTime(
        @Path("coin_id") coinId: String,
        @Query("days") days: String = "max"
    ): PriceChangePerDay

    @GET("{coin_id}/market_chart")
    suspend fun getPriceChangePerYear(
        @Path("coin_id") coinId: String,
        @Query("days") days: String = "360"
    ): PriceChangePerDay

    @GET("{coin_id}/market_chart")
    suspend fun getPriceChangePerMonth(
        @Path("coin_id") coinId: String,
        @Query("days") days: String = "30"
    ): PriceChangePerDay

    @GET("{coin_id}/market_chart")
    suspend fun getPriceChangePerWeek(
        @Path("coin_id") coinId: String,
        @Query("days") days: String = "7"
    ): PriceChangePerDay

    @GET("{coin_id}/market_chart")
    suspend fun getPriceChangePerDay(
        @Path("coin_id") coinId: String,
        @Query("days") days: String = "1"
    ): PriceChangePerDay

    @GET("markets")
    suspend fun getTwentyCoinSortedByPrice(
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int,
        @Query("order") order: String
    ) : CoinListResponse

    @GET("markets")
    suspend fun getTwentyCoinSortedByVolatility(
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int,
        @Query("order") order: String
    ) : CoinListResponse

    @GET("markets")
    suspend fun getTwentyCoinSortedByMarketCap(
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int,
        @Query("order") order: String
    ) : CoinListResponse
}