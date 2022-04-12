package com.example.crypto.repository.interfaces

interface SplashScreenRepository {

    suspend fun fetchingAndCachingInitialCoins() : Boolean
}