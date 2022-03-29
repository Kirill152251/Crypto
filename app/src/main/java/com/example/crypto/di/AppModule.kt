package com.example.crypto.di

import android.content.IntentFilter
import com.example.crypto.model.api.CoinGeckoService
import com.example.crypto.repository.*
import com.example.crypto.repository.interfaces.DetailsScreenRepInterface
import com.example.crypto.repository.interfaces.MainScreenRepInterface
import com.example.crypto.repository.interfaces.SortPreferencesRepInterface
import com.example.crypto.repository.interfaces.SplashScreenRepInterface
import com.example.crypto.view_models.DetailsScreenViewModel
import com.example.crypto.view_models.MainScreenViewModel
import com.example.crypto.view_models.SettingsScreenViewModel
import com.example.crypto.view_models.SplashScreenViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

//https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=20&page=1
private const val CURRENCY = "usd"
private const val BASE_URL = "https://api.coingecko.com/api/v3/coins/"

val appModule = module {
    single { provideRequestInterceptor() }
    single { provideLoggingInterceptor() }
    single { provideClient(get(), get()) }
    single { provideRetrofit(get()) }
    single { provideApiService(get()) }
    single { provideIntentFilter() }
}
val repoCoinsListModule = module {
    single { UserInfoRepository(get()) }
    single<SortPreferencesRepInterface> { SortPreferencesRepositoryImpl(get()) }
    single<MainScreenRepInterface> { MainScreenRepositoryImp(get(), get()) }
    single<SplashScreenRepInterface> { SplashScreenRepositoryImp(get(), get()) }
    single<DetailsScreenRepInterface> { DetailsScreenRepositoryImp(get()) }
}
val viewModels = module {
    viewModel { SplashScreenViewModel(get()) }
    viewModel { (coinId: String) -> DetailsScreenViewModel(get(), coinId) }
    viewModel { SettingsScreenViewModel(get()) }
    viewModel { MainScreenViewModel(get(), get()) }
}

private fun provideRequestInterceptor(): Interceptor {
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

private fun provideLoggingInterceptor(): HttpLoggingInterceptor =
    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)

private fun provideClient(
    requestInterceptor: Interceptor,
    loggingInterceptor: HttpLoggingInterceptor
): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(requestInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()
}

private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

private fun provideApiService(retrofit: Retrofit): CoinGeckoService =
    retrofit.create(CoinGeckoService::class.java)

private fun provideIntentFilter() = IntentFilter()




