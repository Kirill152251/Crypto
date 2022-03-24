package com.example.crypto.viewModels

import androidx.lifecycle.viewModelScope
import com.example.crypto.repository.interfaces.SplashScreenRepInterface
import com.example.crypto.views.fragments.splashScreen.SplashScreenContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashScreenViewModel(private val repository: SplashScreenRepInterface) :
    BaseViewModel<SplashScreenContract.State, SplashScreenContract.Event, SplashScreenContract.Effect>() {

    override fun createInitialState(): SplashScreenContract.State {
        return SplashScreenContract.State(
            SplashScreenContract.CachingInitialCoinsState.Loading
        )
    }


    override fun handleEvent(event: SplashScreenContract.Event) {
        when (event) {
            is SplashScreenContract.Event.LoadingInitialCoins -> {
                cachingInitialCoins()
            }
        }
    }

    private fun cachingInitialCoins() {
        viewModelScope.launch {
            repository.fetchingAndCachingInitialCoins()
            setState { copy(cachingInitialCoinsState = SplashScreenContract.CachingInitialCoinsState.Success) }
        }
    }

}