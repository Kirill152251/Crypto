package com.example.crypto

import android.app.Application
import com.example.crypto.di.appModule
import com.example.crypto.di.viewModels
import com.example.crypto.di.repoCoinsListModule
import com.example.crypto.di.roomModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin { 
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(listOf(appModule, repoCoinsListModule, viewModels, roomModule))
        }
    }
}