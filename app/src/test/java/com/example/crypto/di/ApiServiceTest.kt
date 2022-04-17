package com.example.crypto.di

import com.example.crypto.model.api.CoinGeckoService
import com.example.crypto.MockResponseFileReader
import com.example.crypto.model.api.responses.coins_list.mapToEntity
import com.example.crypto.model.api.responses.price_change.PriceChange
import com.example.crypto.model.db.CoinEntity
import com.example.crypto.utils.ApiResourceForPriceCharts
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

@ExperimentalSerializationApi
class ApiServiceTest {

    private val server = MockWebServer()

    private val httpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()


    private val contentType = "application/json".toMediaType()
    private val json = Json { ignoreUnknownKeys = true }.asConverterFactory(contentType)

    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(server.url("/"))
        .addConverterFactory(json)
        .build()

    private val apiService = retrofit.create(CoinGeckoService::class.java)

    @After
    fun shutdown() {
        server.shutdown()
    }


    @Test
    fun `should fetch correctly given 200 response`() {
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(MockResponseFileReader("success_test_response.json").content)
        )

        runBlocking {
            val actual =
                apiService.getCoinsSortedByMarketCap(1, 1, "test").map { it.mapToEntity() }

            val expected =
                listOf(CoinEntity(0.0, "test", "test", "test", "test", 0, 0.0, 0.0f))

            assertEquals(expected, actual)
        }
    }

    @Test
    fun `should catch HttpException 400 response`() {
        server.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(MockResponseFileReader("success_test_response.json").content)
        )

        runBlocking {
            val actual: ApiResourceForPriceCharts<PriceChange> = try {
                val actual =
                    apiService.getPriceChangePerDay("test")
                ApiResourceForPriceCharts.Success(actual, "", "")
            } catch (e: HttpException) {
                ApiResourceForPriceCharts.Error(e.toString())
            }
            val expected: ApiResourceForPriceCharts<PriceChange> =
                ApiResourceForPriceCharts.Error("retrofit2.HttpException: HTTP 400 Client Error")
            assertEquals(actual, expected)
        }
    }
}