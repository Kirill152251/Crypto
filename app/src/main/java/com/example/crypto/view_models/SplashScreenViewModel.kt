package com.example.crypto.view_models

import androidx.lifecycle.viewModelScope
import com.example.crypto.repository.interfaces.SplashScreenRepository
import com.example.crypto.views.fragments.splash_screen.SplashScreenContract.*
import kotlinx.coroutines.launch

class SplashScreenViewModel(private val repository: SplashScreenRepository) :
    BaseViewModel<State, Event, Effect>() {

    override fun createInitialState(): State {
        return State(
            CachingInitialCoinsState.Loading
        )
    }

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.LoadingInitialCoins -> {
                cachingInitialCoins()
            }
        }
    }

    private fun cachingInitialCoins() {
        viewModelScope.launch {
            repository.fetchingAndCachingInitialCoins()
            setState { copy(cachingInitialCoinsState = CachingInitialCoinsState.Success) }
        }
    }
}