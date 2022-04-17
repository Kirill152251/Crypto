package com.example.crypto.di

import android.content.Context
import androidx.room.Room
import com.example.crypto.model.db.AppDataBase
import org.koin.dsl.module

val roomModule = module {
    single { provideCoinsListDatabase(get()) }
    single { provideCoinsListDao(get()) }
    single { provideUserInfoDao(get()) }
}

private fun provideCoinsListDatabase(context: Context): AppDataBase {
    return Room.databaseBuilder(context, AppDataBase::class.java, "coins")
        .fallbackToDestructiveMigration()
        .build()
}

private fun provideCoinsListDao(appDataBase: AppDataBase) =
    appDataBase.coinsListDao()

private fun provideUserInfoDao(appDataBase: AppDataBase) =
    appDataBase.userInfoDao()