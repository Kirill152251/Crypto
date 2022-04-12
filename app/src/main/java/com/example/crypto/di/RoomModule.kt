package com.example.crypto.di

import android.content.Context
import androidx.room.Room
import com.example.crypto.model.db.CoinsListDataBase
import org.koin.dsl.module

val roomModule = module {
    single { provideCoinsListDatabase(get()) }
    single { provideCoinsListDao(get()) }
    single { provideUserInfoDao(get()) }
}

private fun provideCoinsListDatabase(context: Context): CoinsListDataBase {
    return Room.databaseBuilder(context, CoinsListDataBase::class.java, "coins")
        .fallbackToDestructiveMigration()
        .build()
}

private fun provideCoinsListDao(coinsListDataBase: CoinsListDataBase) =
    coinsListDataBase.coinsListDao()

private fun provideUserInfoDao(coinsListDataBase: CoinsListDataBase) =
    coinsListDataBase.userInfoDao()