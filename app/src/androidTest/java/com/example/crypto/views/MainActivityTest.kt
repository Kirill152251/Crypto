package com.example.crypto.views

import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.crypto.model.api.CoinGeckoService
import com.jakewharton.espresso.OkHttp3IdlingResource
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class MainActivityTest{

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private val mockWebServer = MockWebServer()

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
        .baseUrl("http://127.0.0.1:8080")
        //.addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: CoinGeckoService = retrofit.create(CoinGeckoService::class.java)

    @Before
    fun setup() {
        mockWebServer.start(8080)
        IdlingRegistry.getInstance().register(
            OkHttp3IdlingResource.create(
                "okhttp",
                okHttpClient
            )
        )
    }
    @Test
    fun testSuccessfulResponse() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(200)
                    //.setBody(FileReader.readStringFromFile("success_test_response.json"))
            }
        }
    }
    @After
    fun teardown() {
        mockWebServer.shutdown()
    }
}