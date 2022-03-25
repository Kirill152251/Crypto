package com.example.crypto.views.fragments.splashScreen

import com.example.crypto.model.api.responses.coinsList.CoinListResponse
import com.example.crypto.model.interfacesMVI.UiEffect
import com.example.crypto.model.interfacesMVI.UiEvent
import com.example.crypto.model.interfacesMVI.UiState

class SplashScreenContract {

    sealed class Event: UiEvent {
        object LoadingInitialCoins: Event()
    }

    data class State(
        val cachingInitialCoinsState: CachingInitialCoinsState,
    ): UiState

    sealed class CachingInitialCoinsState {
        object Loading: CachingInitialCoinsState()
        object Success: CachingInitialCoinsState()
    }

    sealed class Effect: UiEffect {}
}