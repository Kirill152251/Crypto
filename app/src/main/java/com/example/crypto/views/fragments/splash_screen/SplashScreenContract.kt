package com.example.crypto.views.fragments.splash_screen

import com.example.crypto.model.interfaces_mvi.UiEffect
import com.example.crypto.model.interfaces_mvi.UiEvent
import com.example.crypto.model.interfaces_mvi.UiState

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
        object Error: CachingInitialCoinsState()
    }

    sealed class Effect: UiEffect {}
}