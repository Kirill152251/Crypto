package com.example.crypto.di

import android.content.Context
import com.example.crypto.model.db.CoinsListDataBase
import org.koin.dsl.module

val roomModule = module {
    single { provideCoinsListDatabase(get()) }
    single { provideCoinsListDao(get()) }
}

fun provideCoinsListDatabase(context: Context) =
    CoinsListDataBase.getDatabase(context)

fun provideCoinsListDao(coinsListDataBase: CoinsListDataBase) =
    coinsListDataBase.coinsListDao()