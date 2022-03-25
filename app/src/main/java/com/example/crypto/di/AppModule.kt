package com.example.crypto.di

import android.content.Context
import android.content.IntentFilter
import com.example.crypto.model.api.CoinGeckoService
import com.example.crypto.repository.*
import com.example.crypto.repository.interfaces.DetailsScreenRepInterface
import com.example.crypto.repository.interfaces.MainScreenRepInterface
import com.example.crypto.repository.interfaces.SortPreferencesRepInterface
import com.example.crypto.repository.interfaces.SplashScreenRepInterface
import com.example.crypto.viewModels.DetailsScreenViewModel
import com.example.crypto.viewModels.MainScreenViewModel
import com.example.crypto.viewModels.SettingsScreenViewModel
import com.example.crypto.viewModels.SplashScreenViewModel
import com.example.crypto.views.fragments.detailsScreen.PriceChartStyle
import com.example.crypto.views.fragments.mainScreen.CoinsListAdapter
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

//https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=20&page=1
private const val CURRENCY = "usd"
private const val BASE_URL = "https://api.coingecko.com/api/v3/coins/"
val apiService = ApiService(BASE_URL)

val appModule = module {
    single { provideRequestInterceptor() }
    single { provideClient(get()) }
    single { provideRetrofit(get()) }
    single { provideApiService(get()) }
    single { provideAdapter(get()) }
    single { providePriceChartStyle(get()) }
    single { provideIntentFilter() }
}
val repoCoinsListModule = module {
    single { SortPreferencesRepositoryImpl(get()) as SortPreferencesRepInterface }
    single { UserInfoRepository(get()) }
    single { MainScreenRepositoryImp(get(), get()) as MainScreenRepInterface }
    single { SplashScreenRepositoryImp(get(), get()) as SplashScreenRepInterface }
    single { DetailsScreenRepositoryImp(get()) as DetailsScreenRepInterface }
}
val viewModels = module {
    viewModel { SplashScreenViewModel(get()) }
    viewModel { (coinId: String) -> DetailsScreenViewModel(get(), coinId) }
    viewModel { SettingsScreenViewModel(get()) }
    viewModel { MainScreenViewModel(get(), get()) }
}

fun provideRequestInterceptor(): Interceptor = apiService.requestInterceptor

fun provideClient(requestInterceptor: Interceptor): OkHttpClient = apiService.okHttpClient

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = apiService.retrofit

fun provideApiService(retrofit: Retrofit): CoinGeckoService = apiService.service

fun provideAdapter(context: Context): CoinsListAdapter = CoinsListAdapter(context)

fun providePriceChartStyle(context: Context): PriceChartStyle = PriceChartStyle(context)

fun provideIntentFilter() = IntentFilter()


class ApiService(baseUrl: String) {
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

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(requestInterceptor)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: CoinGeckoService = retrofit.create(CoinGeckoService::class.java)
}


