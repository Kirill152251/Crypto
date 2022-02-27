package com.example.crypto.di

import android.content.Context
import com.example.crypto.model.api.CoinGeckoService
import com.example.crypto.repository.CoinsListRepository
import com.example.crypto.viewModels.CoinsListViewModel
import com.example.crypto.views.fragments.mainScreen.CoinsListAdapter
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.math.sin

//https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=20&page=1
private const val CURRENCY = "usd"
private const val BASE_URL = "https://api.coingecko.com/api/v3/coins/"


val appModule = module {
    single { provideRequestInterceptor() }
    single { provideClient(get()) }
    single { provideRetrofit(get()) }
    single { provideApiService(get()) }
    single { provideAdapter(get()) }
}
val repoCoinsListModule = module {
    single { CoinsListRepository(get(), get()) }
}
val coinsListViewModel = module {
    viewModel{ CoinsListViewModel(get()) }
}

fun provideRequestInterceptor(): Interceptor {
    val requestInterceptor = Interceptor { chain ->
        val url = chain.request()
            .url
            .newBuilder()
            .addQueryParameter("vs_currency", CURRENCY)
            .build()

        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()
        return@Interceptor chain.proceed(request)
    }
    return requestInterceptor
}
fun provideClient(requestInterceptor: Interceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(requestInterceptor)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()
}
fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
fun provideApiService(retrofit: Retrofit): CoinGeckoService =
    retrofit.create(CoinGeckoService::class.java)

fun provideAdapter(context: Context): CoinsListAdapter = CoinsListAdapter(context)

