package com.example.crypto.viewModels

import androidx.lifecycle.viewModelScope
import com.example.crypto.repository.Repository
import com.example.crypto.views.fragments.splashScreen.SplashScreenContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class SplashScreenViewModel(private val repository: Repository) :
    BaseViewModel<SplashScreenContract.State, SplashScreenContract.Event, SplashScreenContract.Effect>() {

    override fun createInitialState(): SplashScreenContract.State {
        return SplashScreenContract.State(
            SplashScreenContract.CachingInitialCoinsState.Loading
        )
    }


    override fun handleEvent(event: SplashScreenContract.Event) {
        when (event) {
            is SplashScreenContract.Event.CachingInitialCoins -> {
                cachingInitialCoins()
            }
        }
    }

    private fun cachingInitialCoins() {
        viewModelScope.launch(Dispatchers.IO) {
            if (repository.isFetchingAndCachingInitialCoinsDone()) {
                setState { copy(cachingInitialCoinsState = SplashScreenContract.CachingInitialCoinsState.Success) }
            } else {
                setState { copy(cachingInitialCoinsState = SplashScreenContract.CachingInitialCoinsState.Error) }
            }
        }
    }

}