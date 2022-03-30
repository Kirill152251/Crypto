package com.example.crypto.di

import android.content.Context
import androidx.room.Room
import com.example.crypto.model.db.CoinsListDataBase
import com.example.crypto.model.settings_db.UserInfoDataBase
import org.koin.dsl.module

val roomModule = module {
    single { provideCoinsListDatabase(get()) }
    single { provideCoinsListDao(get()) }
    single { provideUserInfoDatabase(get()) }
    single { provideUserInfoDao(get()) }
}

private fun provideCoinsListDatabase(context: Context): CoinsListDataBase {
    return Room.databaseBuilder(context, CoinsListDataBase::class.java, "coins")
        .build()
}

private fun provideCoinsListDao(coinsListDataBase: CoinsListDataBase) =
    coinsListDataBase.coinsListDao()

private fun provideUserInfoDatabase(context: Context): UserInfoDataBase {
    return Room.databaseBuilder(context, UserInfoDataBase::class.java, "user_info")
        .createFromAsset("db/user_info.db")
        .build()
}

private fun provideUserInfoDao(userInfoDataBase: UserInfoDataBase) =
    userInfoDataBase.userInfoDao()