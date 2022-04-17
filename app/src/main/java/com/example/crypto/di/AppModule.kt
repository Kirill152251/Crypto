package com.example.crypto.di

import android.content.IntentFilter
import com.example.crypto.model.api.CoinGeckoService
import com.example.crypto.repository.*
import com.example.crypto.repository.interfaces.*
import com.example.crypto.view_models.DetailsScreenViewModel
import com.example.crypto.view_models.MainScreenViewModel
import com.example.crypto.view_models.SettingsScreenViewModel
import com.example.crypto.view_models.SplashScreenViewModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

//https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=20&page=1
private const val BASE_URL = "https://api.coingecko.com/api/v3/coins/"

val appModule = module {
    single { provideLoggingInterceptor() }
    single { provideClient(get()) }
    single { provideRetrofit(get(), get()) }
    single { provideApiService(get()) }
    single { provideIntentFilter() }
    single { provideBaseUrl() }
}
val repoCoinsListModule = module {
    single<UserInfoRepository> { UserInfoRepositoryImpl(get()) }
    single<SortPreferencesRepository> { SortPreferencesRepositoryImpl(get()) }
    single<MainScreenRepository> { MainScreenRepositoryImpl(get(), get()) }
    single<SplashScreenRepository> { SplashScreenRepositoryImpl(get(), get()) }
    single<DetailsScreenRepository> { DetailsScreenRepositoryImpl(get()) }
}
val viewModels = module {
    viewModel { SplashScreenViewModel(get()) }
    viewModel { (coinId: String) -> DetailsScreenViewModel(get(), coinId) }
    viewModel { SettingsScreenViewModel(get()) }
    viewModel { MainScreenViewModel(get(), get()) }
}

private fun provideLoggingInterceptor(): HttpLoggingInterceptor =
    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)

private fun provideClient(
    loggingInterceptor: HttpLoggingInterceptor
): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()
}

private val contentType = "application/json".toMediaType()
private val json = Json { ignoreUnknownKeys = true }.asConverterFactory(contentType)

private fun provideBaseUrl() = BASE_URL

private fun provideRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit {
    return Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(baseUrl)
        .addConverterFactory(json)
        .build()
}

private fun provideApiService(retrofit: Retrofit): CoinGeckoService =
    retrofit.create(CoinGeckoService::class.java)

private fun provideIntentFilter() = IntentFilter()




