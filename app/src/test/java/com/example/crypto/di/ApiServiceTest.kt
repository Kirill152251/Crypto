package com.example.crypto.di

import com.example.crypto.model.api.CoinGeckoService
import com.example.crypto.model.api.MockResponseFileReader
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiServiceTest {

    private val server = MockWebServer()

    @Before
    fun initTest(){
        val server = MockWebServer()
    }

    @After
    fun shutdown(){
        server.shutdown()
    }

    private val requestInterceptor = Interceptor { chain ->
        val url = chain.request()
            .url
            .newBuilder()
            .build()

        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()
        return@Interceptor chain.proceed(request)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(requestInterceptor)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(server.url("").toString())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: CoinGeckoService = retrofit.create(CoinGeckoService::class.java)

    @Test
    fun test() {
        server.apply {
            enqueue(MockResponse().setBody(MockResponseFileReader("test_json.json").content))
        }

        val response = service.forTests()

        assertEquals(response, "{\"test\": {}}")
    }

}