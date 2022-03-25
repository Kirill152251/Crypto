package com.example.crypto.repository

import com.example.crypto.repository.interfaces.SplashScreenRepInterface

class FakeSplashScreenRepository : SplashScreenRepInterface {


    override suspend fun fetchingAndCachingInitialCoins() {
    }
}