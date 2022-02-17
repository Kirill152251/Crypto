package com.example.crypto

import android.app.Application
import com.example.crypto.di.appModule
import com.example.crypto.di.coinsListViewModel
import com.example.crypto.di.repoCoinsListModule
import com.example.crypto.di.roomModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(appModule, repoCoinsListModule, coinsListViewModel, roomModule))
        }
    }
}