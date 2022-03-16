package com.example.crypto.di

import android.content.Context
import androidx.room.Room
import com.example.crypto.model.db.CoinsListDataBase
import com.example.crypto.model.settingsDB.UserInfoDataBase
import org.koin.dsl.module

val roomModule = module {
    fun provideCoinsListDatabase(context: Context): CoinsListDataBase {
        return Room.databaseBuilder(context, CoinsListDataBase::class.java, "Coins.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    fun provideCoinsListDao(coinsListDataBase: CoinsListDataBase) =
        coinsListDataBase.coinsListDao()

    fun provideUserInfoDatabase(context: Context): UserInfoDataBase {
        return Room.databaseBuilder(context, UserInfoDataBase::class.java, "UserInfo.db")
            .fallbackToDestructiveMigration()
            .createFromAsset("db/user_info.db")
            .build()
    }
    fun provideUserInfoDao(userInfoDataBase: UserInfoDataBase) =
        userInfoDataBase.userInfoDao()

    single { provideCoinsListDatabase(get()) }
    single { provideCoinsListDao(get()) }
    single { provideUserInfoDatabase(get()) }
    single { provideUserInfoDao(get()) }
}