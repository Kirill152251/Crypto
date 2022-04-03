package com.example.crypto.model.api

import com.example.crypto.model.api.responses.coins_list.Coin
import com.example.crypto.model.api.responses.price_change.PriceChangePerDay
import com.example.crypto.model.constans.DEFAULT_CURRENCY
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=20&page=1&sparkline=false
//https://api.coingecko.com/api/v3/coins/bitcoin/market_chart?vs_currency=usd&days=1

interface CoinGeckoService {

    @GET("{coin_id}/market_chart")
    suspend fun getPriceChangeForAllTime(
        @Path("coin_id") coinId: String,
        @Query("days") days: String = "max",
        @Query("vs_currency") currency: String = DEFAULT_CURRENCY
    ): PriceChangePerDay

    @GET("{coin_id}/market_chart")
    suspend fun getPriceChangePerYear(
        @Path("coin_id") coinId: String,
        @Query("days") days: Int = 360,
        @Query("vs_currency") currency: String = DEFAULT_CURRENCY
    ): PriceChangePerDay

    @GET("{coin_id}/market_chart")
    suspend fun getPriceChangePerMonth(
        @Path("coin_id") coinId: String,
        @Query("days") days: Int = 30,
        @Query("vs_currency") currency: String = DEFAULT_CURRENCY
    ): PriceChangePerDay

    @GET("{coin_id}/market_chart")
    suspend fun getPriceChangePerWeek(
        @Path("coin_id") coinId: String,
        @Query("days") days: Int = 7,
        @Query("vs_currency") currency: String = DEFAULT_CURRENCY
    ): PriceChangePerDay

    @GET("{coin_id}/market_chart")
    suspend fun getPriceChangePerDay(
        @Path("coin_id") coinId: String,
        @Query("days") days: Int = 1,
        @Query("vs_currency") currency: String = DEFAULT_CURRENCY
    ): PriceChangePerDay

    @GET("markets")
    suspend fun getCoinsSortedByPrice(
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int,
        @Query("order") order: String,
        @Query("vs_currency") currency: String = DEFAULT_CURRENCY
    ) : List<Coin>

    @GET("markets")
    suspend fun getCoinsSortedByVolatility(
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int,
        @Query("order") order: String,
        @Query("vs_currency") currency: String = DEFAULT_CURRENCY
    ) : List<Coin>

    @GET("markets")
    suspend fun getCoinsSortedByMarketCap(
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int,
        @Query("order") order: String,
        @Query("vs_currency") currency: String = DEFAULT_CURRENCY
    ) : List<Coin>
}