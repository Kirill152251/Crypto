package com.example.crypto.di

import android.content.Context
import androidx.room.Room
import com.example.crypto.model.db.CoinsListDataBase
import org.koin.dsl.module

val roomModule = module {
    fun provideCoinsListDatabase(context: Context): CoinsListDataBase {
        return Room.databaseBuilder(context, CoinsListDataBase::class.java, "Coins.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    fun provideCoinsListDao(coinsListDataBase: CoinsListDataBase) =
        coinsListDataBase.coinsListDao()

    fun provideRemoteKeysDao(coinsListDataBase: CoinsListDataBase) =
        coinsListDataBase.remoteKeysDao()

    single { provideCoinsListDatabase(get()) }
    single { provideCoinsListDao(get()) }
    single { provideRemoteKeysDao(get()) }
}